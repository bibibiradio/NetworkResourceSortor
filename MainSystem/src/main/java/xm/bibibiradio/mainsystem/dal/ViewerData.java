package xm.bibibiradio.mainsystem.dal;

import java.util.Date;

public class ViewerData {
    private long viewerId;
    private String viewerName;
    private int viewerSite;
    private String viewerCategory;
    private int viewerSex;
    private long viewerLevel;
    private String viewerUrl;
    private String viewerShowUrl;
    private int viewCnt;
    private long score;
    private Date gmtInsertTime;
    private Date gmtUpdateTime;
    public long getViewerId() {
        return viewerId;
    }
    public void setViewerId(long viewerId) {
        this.viewerId = viewerId;
    }
    public String getViewerName() {
        return viewerName;
    }
    public void setViewerName(String viewerName) {
        this.viewerName = viewerName;
    }
    public int getViewerSite() {
        return viewerSite;
    }
    public void setViewerSite(int viewerSite) {
        this.viewerSite = viewerSite;
    }
    public String getViewerCategory() {
        return viewerCategory;
    }
    public void setViewerCategory(String viewerCategory) {
        this.viewerCategory = viewerCategory;
    }
    public int getViewerSex() {
        return viewerSex;
    }
    public void setViewerSex(int viewerSex) {
        this.viewerSex = viewerSex;
    }
    public long getViewerLevel() {
        return viewerLevel;
    }
    public void setViewerLevel(long viewerLevel) {
        this.viewerLevel = viewerLevel;
    }
    public String getViewerUrl() {
        return viewerUrl;
    }
    public void setViewerUrl(String viewerUrl) {
        this.viewerUrl = viewerUrl;
    }
    public String getViewerShowUrl() {
        return viewerShowUrl;
    }
    public void setViewerShowUrl(String viewerShowUrl) {
        this.viewerShowUrl = viewerShowUrl;
    }
    public int getViewCnt() {
        return viewCnt;
    }
    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }
    public long getScore() {
        return score;
    }
    public void setScore(long score) {
        this.score = score;
    }
    public Date getGmtInsertTime() {
        return gmtInsertTime;
    }
    public void setGmtInsertTime(Date gmtInsertTime) {
        this.gmtInsertTime = gmtInsertTime;
    }
    public Date getGmtUpdateTime() {
        return gmtUpdateTime;
    }
    public void setGmtUpdateTime(Date gmtUpdateTime) {
        this.gmtUpdateTime = gmtUpdateTime;
    }
    
    
    
}
