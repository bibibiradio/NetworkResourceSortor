package xm.bibibiradio.mainsystem.webservice.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionInterceptor implements HandlerInterceptor {
    final private static Logger LOGGER = Logger.getLogger(SessionInterceptor.class);
    private static SessionManager sessionManager = new LocalSessionManager();
    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler,
                                Exception ex) throws Exception {
        //LOGGER.info("aftreq SessionInterceptor");
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler,
                           ModelAndView mv) throws Exception {
        // TODO Auto-generated method stub
        //LOGGER.info("postreq SessionInterceptor");
        
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
                                                                                            throws Exception {
        // TODO Auto-generated method stub
        //LOGGER.info("prereq SessionInterceptor");
        Cookie[] cookies = req.getCookies();
        String sessionId = null;
        BiSession sess = null;
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("sid")){
                    //LOGGER.info("have sid");
                    sessionId = cookie.getValue();
                }
            }
        }
        
        if(sessionId == null){
            //LOGGER.info("no sid new");
            sess = new BiSession();
            //sess.setNew(true);
            sessionId = sessionManager.genSessionId();
            sess.setId(sessionId);
            sess.setAttribute(SessionManager.SESSION_MNG_KEY, sessionManager);
            sessionManager.setSession(sessionId, sess);
            
            res.addCookie(new Cookie("sid",sessionId));
        }else{
            sess = (BiSession)sessionManager.getSession(sessionId);
            if(sess == null){
                sess = new BiSession();
                sessionId = sessionManager.genSessionId();
                sess.setId(sessionId);
                sess.setAttribute(SessionManager.SESSION_MNG_KEY, sessionManager);
                sessionManager.setSession(sessionId, sess);
                
                res.addCookie(new Cookie("sid",sessionId));
            }else{
                if(isRegenerateSessIdUrl(req.getRequestURI())){
                    sessionId = sessionManager.reGenSessionId(sess);
                    
                    res.addCookie(new Cookie("sid",sessionId));
                }else if(isRemoveSessIdUrl(req.getRequestURI())){
                    sessionManager.removeSession(sessionId);
                    
                    sess = new BiSession();
                    sessionId = sessionManager.genSessionId();
                    sess.setId(sessionId);
                    sess.setAttribute(SessionManager.SESSION_MNG_KEY, sessionManager);
                    sessionManager.setSession(sessionId, sess);
                    
                    res.addCookie(new Cookie("sid",sessionId));
                }
            }
        }
        
        //LOGGER.info("session add req.attr");
        req.setAttribute(SessionManager.SESSION_ATTR_KEY, sess);
        
        return true;
    }
    
    private boolean isRegenerateSessIdUrl(String url){
        LOGGER.info(url);
        if(url.startsWith("/api/login"))
            return true;
        else
            return false;
    }
    
    private boolean isRemoveSessIdUrl(String url){
        LOGGER.info(url);
        if(url.startsWith("/api/logout"))
            return true;
        else
            return false;
    }
    
}
