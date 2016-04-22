package xm.bibibiradio.mainsystem.score;

import org.apache.log4j.Logger;


public class ScorerTestImpl implements IScorer {
	private static Logger logger = Logger.getLogger(ScorerTestImpl.class);
	@Override
	public void start(String configPath) {
		// TODO Auto-generated method stub
		logger.info("scorer start "+configPath);
		
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
