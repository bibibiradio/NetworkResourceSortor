package xm.bibibiradio.mainsystem.webservice.dal;

import java.util.List;

import xm.bibibiradio.mainsystem.webservice.biz.AuthorScoreData;

public interface AuthorDAO {
    public List<AuthorScoreData> selectAuthorListOrderScore(int site,String category,long pageStart,long pageEnd);
}
