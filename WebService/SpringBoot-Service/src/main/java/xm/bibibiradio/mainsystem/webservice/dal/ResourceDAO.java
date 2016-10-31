package xm.bibibiradio.mainsystem.webservice.dal;

import java.util.Date;
import java.util.List;

import xm.bibibiradio.mainsystem.webservice.biz.ResourceScoreData;

public interface ResourceDAO {
    public List<ResourceScoreData> selectResourceListOrderScore(int rType, int rSite,
                                                                String rCategory,
                                                                Date rGmtCreateStart,
                                                                Date rGmtCreateEnd, long pageStart,
                                                                long pageEnd);

    public long selectResourceNum(int rType, int rSite, String rCategory, Date rGmtCreateStart,
                                  Date rGmtCreateEnd);
}
