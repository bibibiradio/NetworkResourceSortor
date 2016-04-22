package xm.bibibiradio.mainsystem.webservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioCommonController;
import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioException;
import xm.bibibiradio.mainsystem.webservice.common.controller.ViewResult;
import xm.bibibiradio.mainsystem.webservice.common.session.Session;
import xm.bibibiradio.mainsystem.webservice.dal.ResourceDAO;
import xm.bibibiradio.mainsystem.webservice.dal.dataobject.MostPvResource;

public class BibibiradioGetMostPvResource extends BibibiRadioCommonController {
    private final static Logger LOGGER = Logger.getLogger(BibibiradioGetMostPvResource.class);
    private ResourceDAO resourceDAO;
    
    @Override
    protected ViewResult handleRequestInternalProxy(HttpServletRequest req,
                                                    HttpServletResponse res, Session session,
                                                    Map<String, String> cookies,
                                                    Map<String, String> params, int templateIndex)
                                                                                                  throws Exception {
        // TODO Auto-generated method stub
        HashMap<String,Object> dalMap;
        if(!isValidateParams(params)){
            throw new BibibiRadioException(-2,"不合法的参数");
        }
        
        try{
            dalMap = replacePage(params);
        }catch(Exception ex){
            LOGGER.error("error message",ex);
            throw new BibibiRadioException(-2,"不合法的参数");
        }
        
        ViewResult viewResult = new ViewResult();
        viewResult.setView("jsonView");
        HashMap<String,Object> result = new HashMap<String,Object>();
        viewResult.setResult(result);
        
        List<MostPvResource> dalResult = resourceDAO.selectPvResource(dalMap);
        result.put("mostPv", dalResult);
        
        return viewResult;
    }
    
    private boolean isValidateParams(Map<String, String> params){
        if(params == null || params.get("page") == null || params.get("pageSize") == null){
            return false;
        }else{
            return true;
        }
    }
    
    private HashMap<String,Object> replacePage(Map<String, String> params){
        long page , pageSize;
        long startPage,endPage;
        
        page = Long.parseLong(params.get("page"));
        pageSize = Long.parseLong(params.get("pageSize"));
        
        startPage = (page - 1)*pageSize +1;
        endPage = startPage + pageSize;
        
        HashMap<String,Object> dalMap = new HashMap<String,Object>();
        
        dalMap.putAll(params);
        dalMap.put("startPage", startPage);
        dalMap.put("endPage", endPage);
        
        return dalMap;
    }

    public ResourceDAO getResourceDAO() {
        return resourceDAO;
    }

    public void setResourceDAO(ResourceDAO resourceDAO) {
        this.resourceDAO = resourceDAO;
    }
    
    
}
