package xm.bibibiradio.mainsystem.dal;

import java.util.Date;

public class ViewerScoreData {
    private long viewerId;
    private String viewerName;
    private long score;
    private String viewerType;
    private String viewerCategory;
    private long viewCnt;
    private long viewResourceNum;
    private Date gmtInsertTime;
    private Date gmtUpdateTime;
    public String getViewerName() {
        return viewerName;
    }
    public void setViewerName(String viewerName) {
        this.viewerName = viewerName;
    }
    public long getScore() {
        return score;
    }
    public void setScore(long score) {
        this.score = score;
    }
    public String getViewerType() {
        return viewerType;
    }
    public void setViewerType(String viewerType) {
        this.viewerType = viewerType;
    }
    public String getViewerCategory() {
        return viewerCategory;
    }
    public void setViewerCategory(String viewerCategory) {
        this.viewerCategory = viewerCategory;
    }
    public long getViewCnt() {
        return viewCnt;
    }
    public void setViewCnt(long viewCnt) {
        this.viewCnt = viewCnt;
    }
    public long getViewResourceNum() {
        return viewResourceNum;
    }
    public void setViewResourceNum(long viewResourceNum) {
        this.viewResourceNum = viewResourceNum;
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
    public long getViewerId() {
        return viewerId;
    }
    public void setViewerId(long viewerId) {
        this.viewerId = viewerId;
    }
}
