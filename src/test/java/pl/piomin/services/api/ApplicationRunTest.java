package pl.piomin.services.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationRunTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void start() {
        String res = restTemplate.getForObject("/hello/{param}", String.class, "123");
        assertNotNull(res);
        assertEquals("Hello", res);
    }
}
