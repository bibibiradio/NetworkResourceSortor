package xm.bibibiradio.mainsystem.webservice.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Resource302Controller {
    final static Logger             LOGGER = Logger.getLogger(Resource302Controller.class);
    

    @RequestMapping("/goto")
    public void greeting(@RequestParam(value = "url", required = true) String url,
                           HttpServletResponse res) {
        res.encodeRedirectURL(url);
    }

}