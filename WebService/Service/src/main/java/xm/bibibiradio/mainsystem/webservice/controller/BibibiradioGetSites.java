package xm.bibibiradio.mainsystem.webservice.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioCommonController;
import xm.bibibiradio.mainsystem.webservice.common.controller.ViewResult;
import xm.bibibiradio.mainsystem.webservice.common.session.Session;
import xm.bibibiradio.mainsystem.webservice.dal.ResourceDAO;

public class BibibiradioGetSites extends BibibiRadioCommonController {
    private final static Logger LOGGER = Logger.getLogger(BibibiradioGetCategories.class);
    private ResourceDAO resourceDAO;
    
    @Override
    protected ViewResult handleRequestInternalProxy(HttpServletRequest req,
                                                    HttpServletResponse res, Session session,
                                                    Map<String, String> cookies,
                                                    Map<String, String> params, int templateIndex)
                                                                                                  throws Exception {
        // TODO Auto-generated method stub
        ViewResult viewResult = new ViewResult();
        viewResult.setView("jsonView");
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("sites", resourceDAO.selectSites());
        viewResult.setResult(result);
        
        return viewResult;
    }

    public ResourceDAO getResourceDAO() {
        return resourceDAO;
    }

    public void setResourceDAO(ResourceDAO resourceDAO) {
        this.resourceDAO = resourceDAO;
    }

}
