package xm.bibibiradio.mainsystem.webservice.biz;

import java.util.ArrayList;
import java.util.List;

import xm.bibibiradio.mainsystem.webservice.dal.ViewerDAO;

public class ViewerScoreBiz {
    private ViewerDAO viewerDAO;
    
    public List<ViewerScoreData> getViewerScore(int site,String category,long page){
        long startPage = (page-1) *20;
        long endPage = startPage + 20;
        
        if(startPage <0 || endPage < 0)
            return new ArrayList<ViewerScoreData>();
        
        
        List<ViewerScoreData> list = viewerDAO.selectViewerListOrderScore(site, category, startPage, endPage);
        
        
        return list;
    }
    
    public ViewerDAO getViewerDAO() {
        return viewerDAO;
    }

    public void setViewerDAO(ViewerDAO viewerDAO) {
        this.viewerDAO = viewerDAO;
    }
    
    
}
