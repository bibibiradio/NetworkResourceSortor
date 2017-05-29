package xm.bibibiradio.mainsystem.webservice.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface CtokenManager {
    public static String CTOKEN_SESSION_KEY = "ctoken";
    public static String CTOKEN_COOKIE_KEY = "ctoken";
    public static String CTOKEN_REQ_KEY = "ctoken";
    public void generateCtoken(HttpServletRequest req,HttpServletResponse res,HttpSession sess) throws Exception;
    public boolean isAccess(HttpServletRequest req,HttpSession sess) throws Exception;
}
