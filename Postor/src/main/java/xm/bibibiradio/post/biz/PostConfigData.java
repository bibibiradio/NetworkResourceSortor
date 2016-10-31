package xm.bibibiradio.post.biz;

import java.util.Date;

public class PostConfigData {
    private long postConfigId;
    private long uid;
    private int postType;
    private int postWay;
    private String postAddress;
    private long postFreq;
    private Date lastPost;
    private int postNum;
    private String postCategory;
    private String postTags;
    private int postLimitDay;
    private int postSite;
    public long getUid() {
        return uid;
    }
    public void setUid(long uid) {
        this.uid = uid;
    }
    public int getPostWay() {
        return postWay;
    }
    public void setPostWay(int postWay) {
        this.postWay = postWay;
    }
    public String getPostAddress() {
        return postAddress;
    }
    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }
    public long getPostFreq() {
        return postFreq;
    }
    public void setPostFreq(long postFreq) {
        this.postFreq = postFreq;
    }
    public Date getLastPost() {
        return lastPost;
    }
    public void setLastPost(Date lastPost) {
        this.lastPost = lastPost;
    }
    public int getPostNum() {
        return postNum;
    }
    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }
    public String getPostCategory() {
        return postCategory;
    }
    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }
    public String getPostTags() {
        return postTags;
    }
    public void setPostTags(String postTags) {
        this.postTags = postTags;
    }
    public int getPostLimitDay() {
        return postLimitDay;
    }
    public void setPostLimitDay(int postLimitDay) {
        this.postLimitDay = postLimitDay;
    }
    public int getPostSite() {
        return postSite;
    }
    public void setPostSite(int postSite) {
        this.postSite = postSite;
    }
    public int getPostType() {
        return postType;
    }
    public void setPostType(int postType) {
        this.postType = postType;
    }
    public long getPostConfigId() {
        return postConfigId;
    }
    public void setPostConfigId(long postConfigId) {
        this.postConfigId = postConfigId;
    }
    
    
}
