package xm.bibibiradio.mainsystem.dal;

public interface ViewerDAO {
    public Long select(String viewerName,int viewerSite);
    public long insert(ViewerData viewerData);
}
