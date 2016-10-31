package xm.bibibiradio.mainsystem.dal;

import java.util.Date;

public class AuthorData {
    private long authorId;
    private String authorName;
    private int authorSite;
    private String authorCategory;
    private int authorSex;
    private long authorLevel;
    private String authorUrl;
    private String authorShowUrl;
    private int resourceNum;
    private long score;
    private Date gmtInsertTime;
    private Date gmtUpdateTime;
    public long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public int getAuthorSite() {
        return authorSite;
    }
    public void setAuthorSite(int authorSite) {
        this.authorSite = authorSite;
    }
    public String getAuthorCategory() {
        return authorCategory;
    }
    public void setAuthorCategory(String authorCategory) {
        this.authorCategory = authorCategory;
    }
    public int getAuthorSex() {
        return authorSex;
    }
    public void setAuthorSex(int authorSex) {
        this.authorSex = authorSex;
    }
    public long getAuthorLevel() {
        return authorLevel;
    }
    public void setAuthorLevel(long authorLevel) {
        this.authorLevel = authorLevel;
    }
    public String getAuthorUrl() {
        return authorUrl;
    }
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }
    public String getAuthorShowUrl() {
        return authorShowUrl;
    }
    public void setAuthorShowUrl(String authorShowUrl) {
        this.authorShowUrl = authorShowUrl;
    }
    public int getResourceNum() {
        return resourceNum;
    }
    public void setResourceNum(int resourceNum) {
        this.resourceNum = resourceNum;
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
