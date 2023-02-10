package pl.piomin.services.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
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
