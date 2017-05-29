package xm.bibibiradio.mainsystem.webservice.security;

import java.security.MessageDigest;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.webservice.util.StaticUtils;

public class BiCtokenManager implements CtokenManager {
    static final Logger LOGGER = Logger.getLogger(BiCtokenManager.class);
    private Random random = new Random();
    @Override
    public void generateCtoken(HttpServletRequest req, HttpServletResponse res,HttpSession sess) throws Exception {
        // TODO Auto-generated method stub
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] needBytes = new byte[16];
            random.nextBytes(needBytes);
            md.update(needBytes);
            byte[] hash = md.digest();
            String rawKey = StaticUtils.bytesToHexString(hash);
            
            sess.setAttribute(CtokenManager.CTOKEN_SESSION_KEY, rawKey);
            res.addCookie(new Cookie(CtokenManager.CTOKEN_COOKIE_KEY,rawKey));
        }catch(Exception ex){
            LOGGER.error("error message",ex);
        }
    }

    @Override
    public boolean isAccess(HttpServletRequest req, HttpSession sess) throws Exception {
        // TODO Auto-generated method stub
        String reqCtoken = req.getParameter(CtokenManager.CTOKEN_REQ_KEY);
        String sessCtoken = (String)sess.getAttribute(CtokenManager.CTOKEN_SESSION_KEY);
        
        if(reqCtoken != null && sessCtoken != null && reqCtoken.equals(sessCtoken))
            return true;
        else
            return false;
                    
    }

}
