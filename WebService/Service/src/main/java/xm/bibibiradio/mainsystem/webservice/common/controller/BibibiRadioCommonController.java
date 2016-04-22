package xm.bibibiradio.mainsystem.webservice.common.controller;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import xm.bibibiradio.mainsystem.webservice.common.security.CSRFValidator;
import xm.bibibiradio.mainsystem.webservice.common.session.Session;
import xm.bibibiradio.mainsystem.webservice.common.session.SessionManager;

/**
 * Controller Proxy
 * @author xiaolei
 * @version 1.0.0
 */
public abstract class BibibiRadioCommonController extends AbstractController {
    //Session管理
    private SessionManager      sessions;
    protected CSRFValidator csrfValidator;
    protected boolean isCsrfValidate;
    
    //json转化配置
    static private JsonConfig jsonConfig;
    final static private Logger logger = Logger.getLogger(BibibiRadioCommonController.class);
    
    static{
        jsonConfig = new JsonConfig();
        
        //Date在json转化成String情况下输出格式为yyyy-MM-dd HH:mm
        jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateProcessor("yyyy-MM-dd HH:mm"));
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse res)
                                                                                                 throws Exception {
        // TODO Auto-generated method stub
        ModelAndView mav = null;

        try {
            //申明
            Map<String, String> params;
            Cookie[] cookies;
            Map<Object, Object> sessionMap;
            Session session;
            int templateIndex = -1;
            String contentType;

            //contentType判断
            contentType = req.getContentType();
            //logger.info("ContentType:"+contentType);
            if (contentType == null || !contentType.equalsIgnoreCase("text/json;charset=utf-8")) {
                mav = new ModelAndView("jsonView");
                mav.addObject("errcode", -2);
                mav.addObject("errmsg", "不支持的请求格式");
                return mav;
            }

            //参数提取
            params = null;
            if (req.getContentLength() > 0) {
                byte[] body = getInputStreamContent(req.getInputStream(), req.getContentLength());
                Map<String, String> middle;
                middle = toJsonFromStrOneLay(new String(body, "UTF-8"));
                templateIndex = Integer.valueOf(middle.get("status"));
                if (middle.get("content") == null) {
                    return null;
                }

                params = toJsonFromStrOneLay(middle.get("content").toString());
            }
            
            //cookie提取
            cookies = req.getCookies();
            HashMap<String, String> mapCookies = new HashMap<String, String>();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    mapCookies.put(cookie.getName(), cookie.getValue());
                }
            }

            //session提取
            sessionMap = null;
            session = new Session();
            String sessionId = mapCookies.get("sessionId");
            if (sessionId != null) {
                sessionMap = sessions.getSession(sessionId);
                session.setSession(sessionMap);
                session.setSessionId(sessionId);
            }else{
                String sid = sessions.genSessionKey();
                sessionMap = new HashMap<Object, Object>();
                sessionMap.put("isValidateCsrf", false);
                
                sessions.setSession(sid, sessionMap);
                res.addCookie(new Cookie("sid", sid));
            }
            
            //csrf验证
            if(csrfValidator != null && isCsrfValidate){
                csrfValidator.validatCsrfToken(params, session);
            }

            //调用实际controller
            ViewResult viewResult = handleRequestInternalProxy(req, res, session, mapCookies,
                params, templateIndex);
            if (viewResult == null || viewResult.getView() == null
                || viewResult.getResult() == null) {
                throw new Exception("Error in view");
            }
            mav = new ModelAndView(viewResult.getView());
            mav.addObject("data", JSONObject.fromObject(viewResult.getResult(),jsonConfig));

            //更新或生成session
            if (!session.isNull() && session.isNeedReGenSessId()) {
                sessions.removeSession(sessionId);
                sessions.setSession(sessions.genSessionKey(), session.getSession());
            } else if (!session.isNull() && session.isDirty()) {
                sessions.setSession(sessionId, session.getSession());
            } else if (session.isNull() && session.isNeedGenSessId()) {
                String sid = sessions.genSessionKey();
                sessions.setSession(sid, new HashMap<Object, Object>());
                res.addCookie(new Cookie("sid", sid));
            }

            mav.addObject("errcode", 0);
            mav.addObject("errmsg", "");

            //返回数据到view层
            return mav;
        } catch (BibibiRadioException ex) {
            logger.error("error message",ex);
            mav = new ModelAndView("jsonView");
            mav.addObject("errcode", ex.getErrCode());
            mav.addObject("errmsg", ex.getErrMsg());
            return mav;
        } catch (Exception ex) {
            logger.error("error message", ex);
            mav = new ModelAndView("jsonView");
            mav.addObject("errcode", -1);
            mav.addObject("errmsg", "出错了！");
            return mav;
        }
    }

    protected abstract ViewResult handleRequestInternalProxy(HttpServletRequest req,
                                                             HttpServletResponse res,
                                                             Session session,
                                                             Map<String, String> cookies,
                                                             Map<String, String> params,
                                                             int templateIndex) throws Exception;

    private byte[] getInputStreamContent(InputStream input, int length) throws Exception {
        if (length > 10000) {
            throw new Exception("ContentLength is so long");
        }
        byte[] inputBytes = new byte[length];
        input.read(inputBytes);
        return inputBytes;
    }

    private Map<String, String> toJsonFromStrOneLay(String jsonStr) throws Exception {
        HashMap<String, String> jsons = new HashMap<String, String>();
        JSONObject jsonObj = JSONObject.fromObject(jsonStr);
        Iterator<Object> iter = jsonObj.keys();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object value = jsonObj.get(key);
            jsons.put(key.toString(), value.toString());
        }
        return jsons;
    }

    public SessionManager getSessions() {
        return sessions;
    }

    public void setSessions(SessionManager sessions) {
        this.sessions = sessions;
    }

    public CSRFValidator getCsrfValidator() {
        return csrfValidator;
    }

    public void setCsrfValidator(CSRFValidator csrfValidator) {
        this.csrfValidator = csrfValidator;
    }

    public boolean isCsrfValidate() {
        return isCsrfValidate;
    }

    public void setCsrfValidate(boolean isCsrfValidate) {
        this.isCsrfValidate = isCsrfValidate;
    }
}
