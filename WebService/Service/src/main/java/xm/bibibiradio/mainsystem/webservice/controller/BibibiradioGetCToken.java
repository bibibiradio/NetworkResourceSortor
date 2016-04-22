package xm.bibibiradio.mainsystem.webservice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioCommonController;
import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioException;
import xm.bibibiradio.mainsystem.webservice.common.controller.ViewResult;
import xm.bibibiradio.mainsystem.webservice.common.session.Session;

public class BibibiradioGetCToken extends BibibiRadioCommonController {
    private final static Logger LOGGER = Logger.getLogger(BibibiradioGetCToken.class);
    
    @Override
    protected ViewResult handleRequestInternalProxy(HttpServletRequest req,
                                                    HttpServletResponse res, Session session,
                                                    Map<String, String> cookies,
                                                    Map<String, String> params, int templateIndex)
                                                                                                  throws Exception {
        // TODO Auto-generated method stub
        ViewResult viewResult = new ViewResult();
        viewResult.setView("jsonView");
        HashMap<String,Object> rt = new HashMap<String,Object>();
        viewResult.setResult(rt);
        
        if(csrfValidator == null){
            throw new BibibiRadioException(-1,"内部错误");
        }
        
        rt.put("ctoken", csrfValidator.generateCsrfToken(session));
        
        session.setNeedReGenSessId(true);
        
        return viewResult;
    }

}
