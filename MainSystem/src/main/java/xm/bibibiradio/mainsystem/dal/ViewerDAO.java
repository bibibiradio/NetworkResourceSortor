package xm.bibibiradio.mainsystem.dal;

public interface ViewerDAO {
    public ViewerData select(String viewerName,String viewerType);
    public long insert(ViewerData viewerData);
}
