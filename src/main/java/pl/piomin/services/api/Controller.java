package pl.piomin.services.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    protected Logger logger = LoggerFactory.getLogger(Controller.class.getName());

    @RequestMapping("/hello/{param}")
    public String hello(@PathVariable("param") String param) {
        logger.info("Controller.hello(" + param + ")");
        return "Hello";
    }

}
