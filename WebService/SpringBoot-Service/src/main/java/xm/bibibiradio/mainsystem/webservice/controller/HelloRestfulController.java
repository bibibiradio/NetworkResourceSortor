package xm.bibibiradio.mainsystem.webservice.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestfulController {
    final static Logger             LOGGER = Logger.getLogger(HelloRestfulController.class);
    
    @RequestMapping("/api/hello")
    public RestfulResult greeting(@RequestParam(value = "name", required = false, defaultValue = "xl") String name) {
        LOGGER.info("GOOD MORNING ------------------------");
        return RestfulResultUtil.newRestfulResult("hello "+name);
    }
}   
