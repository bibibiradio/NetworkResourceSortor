package xm.bibibiradio.mainsystem.dal;

public interface ViewDAO {
	public long insertView(ViewData viewData);
	public void deleteAll();
	public ViewData selectMaxFloor(long resourceId);
}
