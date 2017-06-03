package xm.bibibiradio.mainsystem.starter;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.dal.ResourceDAO;
import xm.bibibiradio.mainsystem.dal.ViewDAO;

public class DeleteInvalidTimeDataStarter extends Thread {
    private ResourceDAO resourceDAO;
    private ViewDAO viewDAO;
    private String configPath;
    private static Properties conf;
    final static private int[] rSites = {1};
    final static private Logger LOGGER = Logger.getLogger(DeleteInvalidTimeDataStarter.class);
    @Override
    public void run(){
        long lastDeleteTime = 0;
        try{
            if(conf == null){
                conf = Resources.getResourceAsProperties(configPath);                
            }
        }catch(Exception ex){
            LOGGER.error("error",ex);
            return;
        }
        
        while(true){
            try{
                if(System.currentTimeMillis() - lastDeleteTime > 24*60*60*1000L){
                    delete(new Date(System.currentTimeMillis()-Long.parseLong(conf.getProperty("lastStoreTime",""))));
                    
                    lastDeleteTime = System.currentTimeMillis();
                }
                
                Thread.sleep(60*60*1000L);
            }catch(Exception ex){
                LOGGER.error("error",ex);
            }
        }
    }
    
    public void delete(Date lastStoreTime){
        Date startStoreTime = new Date(0);
        
        LOGGER.info(lastStoreTime.toString());
        LOGGER.info(startStoreTime.toString());
        
        for(int rSite : rSites){
	        List<Long> rIds = resourceDAO.selectDateList(startStoreTime, lastStoreTime,rSite);
	        
	        LOGGER.info("rIds have "+rIds.size());
	        
	        for(Long rId : rIds){
	            viewDAO.deleteRid(rId);
	            resourceDAO.deleteRid(rId);
	        }
        }
    }

    public ResourceDAO getResourceDAO() {
        return resourceDAO;
    }

    public void setResourceDAO(ResourceDAO resourceDAO) {
        this.resourceDAO = resourceDAO;
    }

    public ViewDAO getViewDAO() {
        return viewDAO;
    }

    public void setViewDAO(ViewDAO viewDAO) {
        this.viewDAO = viewDAO;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
    
    
}
