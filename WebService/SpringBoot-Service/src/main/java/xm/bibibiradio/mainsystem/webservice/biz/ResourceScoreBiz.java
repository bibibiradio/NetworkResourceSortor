package xm.bibibiradio.mainsystem.webservice.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xm.bibibiradio.mainsystem.webservice.dal.ResourceDAO;

public class ResourceScoreBiz {
    private ResourceDAO resourceDAO;
    
    public List<ResourceScoreData> getResourceScore(int type,int site,String category,long page,int dayLimit,String tag){
        long startPage = (page-1) *20;
        long endPage = startPage + 20;
        
        if(startPage <0 || endPage < 0)
            return new ArrayList<ResourceScoreData>();
        
        
        List<ResourceScoreData> list = resourceDAO.selectResourceListOrderScore(type, site, category, new Date(System.currentTimeMillis() - dayLimit*24*60*60*1000L), new Date(), startPage, endPage,tag);
        
        if(site == 1){
            for(ResourceScoreData data : list){
                data.setResourceShowUrl(data.getResourceShowUrl()+"_160x100.jpg");
            }
        }
        
        return list;
    }
    
    public long getResourceNum(int type,int site,String category,int dayLimit,String tag){
        return resourceDAO.selectResourceNum(type, site, category, new Date(System.currentTimeMillis() - dayLimit*24*60*60*1000L), new Date(),tag);
    }

    public ResourceDAO getResourceDAO() {
        return resourceDAO;
    }

    public void setResourceDAO(ResourceDAO resourceDAO) {
        this.resourceDAO = resourceDAO;
    }
    
    
}
