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
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
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
            return BindingBuilder.bind(queue).to(exchange).with("#");
        }
    }

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Container
    static final GenericContainer rabbitmq = new FixedHostPortGenericContainer<>("rabbitmq:3")
            .withFixedExposedPort(5672, 5672)
            .withExposedPorts(5672);

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("app.amqp.url", () -> "localhost:5672");
    }


    @Test
    public void start() throws InterruptedException {
        rabbitTemplate.start();
        restTemplate.getForObject("/hello/{param}", String.class, "123");
        rabbitTemplate.receive("ex_logstash", 3000);
        Thread.sleep(3000);

        for (int i = 0; i < 10; i++) {
            sendLogAndReceiveMessage();
        }

    }


    private void sendLogAndReceiveMessage() {

        String res = restTemplate.getForObject("/hello/{param}", String.class, "123");
        assertNotNull(res);
        assertEquals("Hello", res);

        Message msg = rabbitTemplate.receive("ex_logstash", 3000);
        assertNotNull(msg);
    }
}
