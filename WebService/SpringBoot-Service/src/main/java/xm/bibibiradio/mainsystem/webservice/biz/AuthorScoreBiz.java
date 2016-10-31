package xm.bibibiradio.mainsystem.webservice.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xm.bibibiradio.mainsystem.webservice.dal.AuthorDAO;

public class AuthorScoreBiz {
    AuthorDAO authorDAO;
    
    public List<AuthorScoreData> getAuthorsScore(int site,String category,long page){
        long startPage = (page-1) *20;
        long endPage = startPage + 20;
        
        if(startPage <0 || endPage < 0)
            return new ArrayList<AuthorScoreData>();
        
        
        List<AuthorScoreData> list = authorDAO.selectAuthorListOrderScore(site, category, startPage, endPage);
        
        
        return list;
    }
    
    public AuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    public void setAuthorDAO(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }
    
    
}
