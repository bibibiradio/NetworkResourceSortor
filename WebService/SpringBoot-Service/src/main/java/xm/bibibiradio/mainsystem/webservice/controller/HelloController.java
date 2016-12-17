package xm.bibibiradio.mainsystem.webservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xm.bibibiradio.mainsystem.webservice.session.SessionManager;

@Controller
public class HelloController {
    
    @RequestMapping("/hello")
    public String greeting(HttpServletRequest req,
                           @RequestParam(value = "name", required = false) String name,Model model) {
        HttpSession httpSession = (HttpSession)req.getAttribute(SessionManager.SESSION_ATTR_KEY);
        String ret = (String) httpSession.getAttribute("name");
        if(ret == null)
            ret = "";
        if(name != null)
            httpSession.setAttribute("name", name);
        model.addAttribute("name", ret);
        return "hello";
    }
}
