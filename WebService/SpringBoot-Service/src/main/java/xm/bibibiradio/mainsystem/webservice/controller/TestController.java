package xm.bibibiradio.mainsystem.webservice.controller;



import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    final static Logger           LOGGER = Logger.getLogger(TestController.class);
    

    @RequestMapping("/t")
    public String greeting(HttpServletRequest req,
                           Model model) {
        String aa= req.getParameter("a");
        LOGGER.info(aa);
        return "{}";
    }
}
