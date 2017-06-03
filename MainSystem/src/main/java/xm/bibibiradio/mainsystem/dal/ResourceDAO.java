package xm.bibibiradio.mainsystem.dal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ResourceDAO {
	public Long selectMaxDataBySite(int rType,int rSite);
	public Long selectMinDataBySite(int rType,int rSite);
	public long insertResource(ResourceData rData);
	public List<Long> selectDateSiteList(Date startDate,Date endDate,int rSite);
	public void updateResource(ResourceData resourceData);
	public HashMap<String,Object> selectCommentByRid(long rId);
	public List<Long> selectDateList(Date startDate,Date endDate,int rSite);
	public void deleteRid(long rId);
	
	public Long selectByrInnerId(long rInnerId,int rSite);
}
