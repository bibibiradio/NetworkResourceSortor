package xm.bibibiradio.mainsystem.webservice.dal.dataobject;

import java.util.Date;

public class MostPvResource {
    private String title;
    private String up;
    private String imgUrl;
    private String resourceUrl;
    private String category;
    private String website;
    private Date createDate;
    private long pv;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUp() {
        return up;
    }
    public void setUp(String up) {
        this.up = up;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getResourceUrl() {
        return resourceUrl;
    }
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public long getPv() {
        return pv;
    }
    public void setPv(long pv) {
        this.pv = pv;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    
}
