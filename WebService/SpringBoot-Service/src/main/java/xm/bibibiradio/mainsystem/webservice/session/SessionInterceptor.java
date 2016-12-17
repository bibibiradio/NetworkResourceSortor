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
        // TODO Auto-generated method stub
        LOGGER.info("afterHandler");
        BiSession sess = (BiSession)req.getAttribute(SessionManager.SESSION_ATTR_KEY);
        if(sess == null)
            return;
        
        if(sess.isNew()){
            String sessionId = sessionManager.genSessionId();
            sessionManager.setSession(sessionId, sess);
            sess.setNew(false);
            res.addCookie(new Cookie("sid",sessionId));
        }else if(sess.getAttribute(SessionManager.SESSIONID_REG_KEY) != null){
            String sessionId = sessionManager.reGenSessionId(sess);
            sess.removeAttribute(SessionManager.SESSIONID_REG_KEY);
            res.addCookie(new Cookie("sid",sessionId));
        }
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler,
                           ModelAndView mv) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
                                                                                            throws Exception {
        // TODO Auto-generated method stub
        LOGGER.info("preHandler");
        Cookie[] cookies = req.getCookies();
        String sessionId = null;
        BiSession sess = null;
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("sid")){
                    sessionId = cookie.getValue();
                }
            }
        }
        
        if(sessionId == null){
            sess = new BiSession();
            sess.setNew(true);
            sessionId = sessionManager.genSessionId();
            sess.setId(sessionId);
            sess.setAttribute(SessionManager.SESSION_MNG_KEY, sessionManager);
            sessionManager.setSession(sessionId, sess);
        }else{
            sess = (BiSession)sessionManager.getSession(sessionId);
            if(sess == null){
                sess = new BiSession();
                sess.setNew(true);
                sessionId = sessionManager.genSessionId();
                sess.setId(sessionId);
                sess.setAttribute(SessionManager.SESSION_MNG_KEY, sessionManager);
                sessionManager.setSession(sessionId, sess);
            }
        }
        
        req.setAttribute(SessionManager.SESSION_ATTR_KEY, sess);
        
        return true;
    }

}
