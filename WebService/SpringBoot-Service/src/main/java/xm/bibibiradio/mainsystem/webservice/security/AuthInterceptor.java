package xm.bibibiradio.mainsystem.webservice.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import xm.bibibiradio.mainsystem.webservice.session.SessionManager;
import xm.bibibiradio.mainsystem.webservice.util.RawJsonResUtil;


public class AuthInterceptor implements HandlerInterceptor {
    final private static Logger LOGGER = Logger.getLogger(AuthInterceptor.class);
    private String sessionPosKey = SessionManager.SESSION_ATTR_KEY;
    private String sessionUidKey = "uid";
    
    private AuthManager authManager = new BiAuthManager();
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
                                Exception arg3) throws Exception {
        // TODO Auto-generated method stub
        //LOGGER.info("aftreq AuthInterceptor");
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
                           ModelAndView arg3) throws Exception {
        // TODO Auto-generated method stub
        //LOGGER.info("postreq AuthInterceptor");
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
                                                                                            throws Exception {
        // TODO Auto-generated method stub
        //LOGGER.info("prereq AuthInterceptor");
        HttpSession sess = (HttpSession)req.getAttribute(sessionPosKey);
        if(sess == null){
            RawJsonResUtil.failRes(res, "auth error");
            return false;
        }
        
        String uid = (String)sess.getAttribute(sessionUidKey);
        if(uid == null){
            RawJsonResUtil.failRes(res, "auth error");
            return false;
        }
        
        String uri = req.getRequestURI();
        if(uri == null){
            RawJsonResUtil.failRes(res, "auth error");
            return false;
        }
        
        if(!authManager.isAccess(uid, uri)){
            RawJsonResUtil.failRes(res, "auth error");
            return false;
        }
        
        return true;
    }

}
