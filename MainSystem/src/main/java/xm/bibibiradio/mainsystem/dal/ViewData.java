package xm.bibibiradio.mainsystem.dal;

import java.util.Date;

public class ViewData {
	private long viewId;
	private long resourceId;
	private String viewerName;
	private String viewType;
	private int viewerSource;
	private String viewContent;
	private Date gmtInsertTime;
	private Date gmtViewTime;
	private int floor;
	private int replyCount;
	private long viewerId;
	public long getViewId() {
		return viewId;
	}
	public void setViewId(long viewId) {
		this.viewId = viewId;
	}
	public String getViewerName() {
		return viewerName;
	}
	public void setViewerName(String viewerName) {
		this.viewerName = viewerName;
	}
	public int getViewerSource() {
		return viewerSource;
	}
	public void setViewerSource(int viewerSource) {
		this.viewerSource = viewerSource;
	}
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public String getViewContent() {
		return viewContent;
	}
	public void setViewContent(String viewContent) {
		this.viewContent = viewContent;
	}
	public Date getGmtViewTime() {
		return gmtViewTime;
	}
	public void setGmtViewTime(Date gmtViewTime) {
		this.gmtViewTime = gmtViewTime;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public Date getGmtInsertTime() {
		return gmtInsertTime;
	}
	public void setGmtInsertTime(Date gmtInsertTime) {
		this.gmtInsertTime = gmtInsertTime;
	}
    public String getViewType() {
        return viewType;
    }
    public void setViewType(String viewType) {
        this.viewType = viewType;
    }
    public long getViewerId() {
        return viewerId;
    }
    public void setViewerId(long viewerId) {
        this.viewerId = viewerId;
    }
	
}
