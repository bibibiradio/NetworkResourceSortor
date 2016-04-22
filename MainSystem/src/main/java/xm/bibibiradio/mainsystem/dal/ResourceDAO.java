package xm.bibibiradio.mainsystem.dal;

import java.util.Date;

public interface ResourceDAO {
	public ResourceData selectMaxDataByType(String rType);
	public long insertResource(ResourceData rData);
	public ResourceData selectDateTypeFirst(Date startDate,Date endDate,String rType);
	public ResourceData selectDateTypeEnd(Date startDate,Date endDate,String rType);
	public void updateResource(ResourceData resourceData);
}
