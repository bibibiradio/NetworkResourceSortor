package xm.bibibiradio.mainsystem.webservice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioCommonController;
import xm.bibibiradio.mainsystem.webservice.common.controller.ViewResult;
import xm.bibibiradio.mainsystem.webservice.common.session.Session;

public class BibibiradioJsonTestController extends BibibiRadioCommonController {
    private final static Logger LOGGER = Logger.getLogger(BibibiradioJsonTestController.class);
    @Override
    protected ViewResult handleRequestInternalProxy(HttpServletRequest req, 
                                                      HttpServletResponse res,
                                                      Session session,
                                                      Map<String, String> cookies,
                                                      Map<String, String> params,
                                                      int templateIndex) throws Exception {
        // TODO Auto-generated method stub
        ViewResult viewResult = new ViewResult();
        if(templateIndex == -1){
            return null;
        }
        
        HashMap<String,Object> data = new HashMap<String,Object>();
        
        if(templateIndex == 0){
            viewResult.setView("jsonView");
            viewResult.setResult(data);
        }else{
            
        }
        
        data.put("name1",params.get("name").toString());
        data.put("name2","xiaolei");
        return viewResult;
    }

}
