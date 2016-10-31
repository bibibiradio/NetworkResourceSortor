package xm.bibibiradio.mainsystem.dal;

import java.util.Date;

public class ViewData {
    private long   viewId;
    private long   rId;
    private long   viewerId;

    private int    floor;
    private String viewContent;
    private Date   gmtInsertTime;
    private Date   gmtViewTime;

    private int    replyCount;

    public long getViewId() {
        return viewId;
    }

    public void setViewId(long viewId) {
        this.viewId = viewId;
    }

    public long getrId() {
        return rId;
    }

    public void setrId(long rId) {
        this.rId = rId;
    }

    public long getViewerId() {
        return viewerId;
    }

    public void setViewerId(long viewerId) {
        this.viewerId = viewerId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getViewContent() {
        return viewContent;
    }

    public void setViewContent(String viewContent) {
        this.viewContent = viewContent;
    }

    public Date getGmtInsertTime() {
        return gmtInsertTime;
    }

    public void setGmtInsertTime(Date gmtInsertTime) {
        this.gmtInsertTime = gmtInsertTime;
    }

    public Date getGmtViewTime() {
        return gmtViewTime;
    }

    public void setGmtViewTime(Date gmtViewTime) {
        this.gmtViewTime = gmtViewTime;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }
    
    

}
