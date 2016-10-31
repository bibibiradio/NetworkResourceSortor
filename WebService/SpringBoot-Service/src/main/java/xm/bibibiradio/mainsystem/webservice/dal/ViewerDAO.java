package xm.bibibiradio.mainsystem.webservice.dal;

import java.util.List;

import xm.bibibiradio.mainsystem.webservice.biz.ViewerScoreData;

public interface ViewerDAO {
    public List<ViewerScoreData> selectViewerListOrderScore(int site,String category,long pageStart,long pageEnd);
}
