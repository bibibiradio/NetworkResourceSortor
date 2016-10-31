package xm.bibibiradio.mainsystem.webservice.biz;

public class AuthorScoreData {
    private String authorName;
    private String authorUrl;
    private String authorShowUrl;
    private int resourceNum;
    private int site;
    private String category;
    private long score;
    private long level;
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
    public long getLevel() {
        return level;
    }
    public void setLevel(long level) {
        this.level = level;
    }
    
    
}
