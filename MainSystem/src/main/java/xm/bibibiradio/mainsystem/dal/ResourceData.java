package xm.bibibiradio.mainsystem.dal;

import java.util.Date;

public class ResourceData {
    private long   rId;
    private String rTitle;
    private String rCategory;
    private int    rType;
    private int    rSite;
    private long   authorId;
    private long   rDuration;
    private long   rPv;
    private long   rComment;
    private long   rDanmu;
    private long   rCollect;
    private String rTags;
    private String rUrl;
    private String rShowUrl;
    private long   rCoin;
    private int    isValid;
    private String otherDetail;
    private Date   rGmtCreate;
    private Date   itemGmtInsert;
    private long   score;
    private long   rInnerId;

    public long getrId() {
        return rId;
    }

    public void setrId(long rId) {
        this.rId = rId;
    }

    public String getrCategory() {
        return rCategory;
    }

    public void setrCategory(String rCategory) {
        this.rCategory = rCategory;
    }

    public int getrType() {
        return rType;
    }

    public void setrType(int rType) {
        this.rType = rType;
    }

    public int getrSite() {
        return rSite;
    }

    public void setrSite(int rSite) {
        this.rSite = rSite;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getrDuration() {
        return rDuration;
    }

    public void setrDuration(long rDuration) {
        this.rDuration = rDuration;
    }

    public long getrPv() {
        return rPv;
    }

    public void setrPv(long rPv) {
        this.rPv = rPv;
    }

    public long getrComment() {
        return rComment;
    }

    public void setrComment(long rComment) {
        this.rComment = rComment;
    }

    public long getrDanmu() {
        return rDanmu;
    }

    public void setrDanmu(long rDanmu) {
        this.rDanmu = rDanmu;
    }

    public long getrCollect() {
        return rCollect;
    }

    public void setrCollect(long rCollect) {
        this.rCollect = rCollect;
    }

    public String getrTags() {
        return rTags;
    }

    public void setrTags(String rTags) {
        this.rTags = rTags;
    }

    public String getrUrl() {
        return rUrl;
    }

    public void setrUrl(String rUrl) {
        this.rUrl = rUrl;
    }

    public String getrShowUrl() {
        return rShowUrl;
    }

    public void setrShowUrl(String rShowUrl) {
        this.rShowUrl = rShowUrl;
    }

    public long getrCoin() {
        return rCoin;
    }

    public void setrCoin(long rCoin) {
        this.rCoin = rCoin;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getOtherDetail() {
        return otherDetail;
    }

    public void setOtherDetail(String otherDetail) {
        this.otherDetail = otherDetail;
    }

    public Date getrGmtCreate() {
        return rGmtCreate;
    }

    public void setrGmtCreate(Date rGmtCreate) {
        this.rGmtCreate = rGmtCreate;
    }

    public Date getItemGmtInsert() {
        return itemGmtInsert;
    }

    public void setItemGmtInsert(Date itemGmtInsert) {
        this.itemGmtInsert = itemGmtInsert;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getrTitle() {
        return rTitle;
    }

    public void setrTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public long getrInnerId() {
        return rInnerId;
    }

    public void setrInnerId(long rInnerId) {
        this.rInnerId = rInnerId;
    }

}
