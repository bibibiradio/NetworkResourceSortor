package xm.bibibiradio.post.biz;

import java.util.Date;

public class PostResourceData {
    private long rId;
    private String title;
    private int site;
    private String category;
    private int type;
    private String resourceShowUrl;
    private String resourceUrl;
    private String authorName;
    private String authorUrl;
    private long pv;
    private long resourceScore;
    private long duration;
    private long commentNum;
    private Date createDate;
    private long score;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getResourceShowUrl() {
        return resourceShowUrl;
    }
    public void setResourceShowUrl(String resourceShowUrl) {
        this.resourceShowUrl = resourceShowUrl;
    }
    public String getResourceUrl() {
        return resourceUrl;
    }
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getAuthorUrl() {
        return authorUrl;
    }
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }
    public long getPv() {
        return pv;
    }
    public void setPv(long pv) {
        this.pv = pv;
    }
    public long getResourceScore() {
        return resourceScore;
    }
    public void setResourceScore(long resourceScore) {
        this.resourceScore = resourceScore;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public long getrId() {
        return rId;
    }
    public void setrId(long rId) {
        this.rId = rId;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public long getCommentNum() {
        return commentNum;
    }
    public void setCommentNum(long commentNum) {
        this.commentNum = commentNum;
    }
    public int getSite() {
        return site;
    }
    public void setSite(int site) {
        this.site = site;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getScore() {
        return score;
    }
    public void setScore(long score) {
        this.score = score;
    }
}
