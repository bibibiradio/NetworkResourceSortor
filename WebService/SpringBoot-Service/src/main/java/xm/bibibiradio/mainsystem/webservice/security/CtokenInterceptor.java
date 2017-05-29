package xm.bibibiradio.mainsystem.webservice.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import xm.bibibiradio.mainsystem.webservice.session.SessionManager;
import xm.bibibiradio.mainsystem.webservice.util.RawJsonResUtil;

public class CtokenInterceptor implements HandlerInterceptor {
    
    private CtokenManager ctokenManager;
    
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
                                Exception arg3) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
                           ModelAndView arg3) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
                                                                                            throws Exception {
        // TODO Auto-generated method stub
        if(isGenUri(req.getRequestURI())){
            ctokenManager.generateCtoken(req, res, (HttpSession)req.getAttribute(SessionManager.SESSION_ATTR_KEY));
            return false;
        }
        
        if(!ctokenManager.isAccess(req, (HttpSession)req.getAttribute(SessionManager.SESSION_ATTR_KEY))){
            RawJsonResUtil.failRes(res, "ctoken error");
            return false;
        }
        
        return true;
    }
    
    private boolean isGenUri(String uri){
        if(uri.startsWith("/api/getCtoken"))
            return true;
        else
            return false;
    }
    
    
}
