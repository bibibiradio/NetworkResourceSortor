package xm.bibibiradio.mainsystem.dal;

public interface ViewerScoreDAO {
    public long insert(ViewerScoreData viewerScoreData);
    public ViewerScoreData select(String viewerName,String viewerType);
    public void update(String viewerName,String viewerType,long score);
}
