package xm.bibibiradio.mainsystem.webservice.util;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import xm.bibibiradio.mainsystem.webservice.controller.RestfulResultUtil;

public class RawJsonResUtil {
    static public void failRes(HttpServletResponse res,String errMsg) throws Exception{
        
        String json = JSONObject.fromObject(RestfulResultUtil.newErrorRestfulResult(new Exception(errMsg))).toString();
        if(json != null){
            res.getWriter().append(json);
        }
    }
    
    static public void sucRes(HttpServletResponse res,Object data) throws Exception{
        String json = JSONObject.fromObject(RestfulResultUtil.newRestfulResult(data)).toString();
        if(json != null){
            res.getWriter().append(json);
        }
    }
}
