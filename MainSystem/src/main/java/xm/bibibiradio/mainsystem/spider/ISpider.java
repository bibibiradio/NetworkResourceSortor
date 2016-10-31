package xm.bibibiradio.mainsystem.spider;

public interface ISpider {
	public void startForward(String configPath) throws Exception;
	public void updateNow(String configPath) throws Exception;
	public void startAfter(String configPath) throws Exception;
	public void init(String configPath) throws Exception;
}
