package xm.bibibiradio.mainsystem.spider;

import org.apache.log4j.Logger;

public class SpiderTestImpl implements ISpider {
	private static Logger logger = Logger.getLogger(SpiderTestImpl.class);
	@Override
	public void start(String configPath) {
		// TODO Auto-generated method stub
		logger.info("spider start "+configPath);
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    @Override
    public void update(String configPath) {
        // TODO Auto-generated method stub
        
    }

}
