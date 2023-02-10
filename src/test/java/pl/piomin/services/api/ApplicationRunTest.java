package pl.piomin.services.api;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ApplicationRunTest {

    @TestConfiguration
    public static class RabbitConfiguration {
        @Bean
        public Queue queue() {
            return new Queue("ex_logstash");
        }

        @Bean
        TopicExchange exchange() {
            return new TopicExchange("ex_logstash");
        }

        @Bean
        Binding binding(Queue queue, TopicExchange exchange) {
            return BindingBuilder.bind(queue).to(exchange).with("api-service-4");
        }
    }

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Container
    static final RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:latest")
            .withExposedPorts(5672);

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("app.amqp.url", rabbitmq::getAmqpUrl);
    }

    @Test
    public void start() {
        String res = restTemplate.getForObject("/hello/{param}", String.class, "123");
        assertNotNull(res);
        assertEquals("Hello", res);

//        Message msg = rabbitTemplate.receive("ex_logstash");
//        System.out.println("MSG: " + msg);
    }
}
