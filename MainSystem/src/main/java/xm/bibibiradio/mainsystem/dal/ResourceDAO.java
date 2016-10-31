package xm.bibibiradio.mainsystem.dal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ResourceDAO {
	public Long selectMaxDataBySite(int site);
	public Long selectMinDataBySite(int site);
	public long insertResource(ResourceData rData);
	public List<Long> selectDateSiteList(Date startDate,Date endDate,int rSite);
	public void updateResource(ResourceData resourceData);
	public HashMap<String,Object> selectCommentByRid(long rId);
}
