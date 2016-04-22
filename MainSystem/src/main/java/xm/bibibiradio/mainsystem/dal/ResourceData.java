package xm.bibibiradio.mainsystem.dal;

import java.util.Date;

public class ResourceData {
	private long rId;
	private String rType;
	private int rSource;
	private String rCategory;
	private String rTitle;
	private String author;
	private long rDuration;
	private long rPv;
	private long rComment;
	private long rDanmu;
	private long rCollect;
	private String rTags;
	private long rCoin;
	private int isValid;
	private String otherDetail;
	private Date rGmtCreate;
	private Date rGmtInsert;
	private String rUrl;
	private String rShowUrl;
	private long rSocre;
	private long authorId;
	
	
	public String getrTitle() {
		return rTitle;
	}
	public void setrTitle(String rTitle) {
		this.rTitle = rTitle;
	}
	public long getrSocre() {
		return rSocre;
	}
	public void setrSocre(long rSocre) {
		this.rSocre = rSocre;
	}
	public long getrId() {
		return rId;
	}
	public void setrId(long rId) {
		this.rId = rId;
	}
	public String getrType() {
		return rType;
	}
	public void setrType(String rType) {
		this.rType = rType;
	}
	public int getrSource() {
		return rSource;
	}
	public void setrSource(int rSource) {
		this.rSource = rSource;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
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
	public Date getrGmtInsert() {
		return rGmtInsert;
	}
	public void setrGmtInsert(Date rGmtInsert) {
		this.rGmtInsert = rGmtInsert;
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
	public String getrCategory() {
		return rCategory;
	}
	public void setrCategory(String rCategory) {
		this.rCategory = rCategory;
	}
    public long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
}
