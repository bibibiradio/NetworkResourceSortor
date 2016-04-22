package xm.bibibiradio.mainsystem.starter;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.score.IScorer;
import xm.bibibiradio.mainsystem.util.SimpleTimer;

public class ScoreManager extends Thread {
    private static Logger logger = Logger.getLogger(ScoreManager.class);
	private ArrayList<IScorer> scores = null;
	//private ArrayList<Thread> scoreStarters = null;
	private String configPath = null;
	private static Properties conf;
	private long cyctime;
	private SimpleTimer timer;
	
	public ArrayList<IScorer> getScores() {
        return scores;
    }
    public void setScores(ArrayList<IScorer> scores) {
        this.scores = scores;
    }
    public String getConfigPath() {
		return configPath;
	}
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}
	
//	private void initScoreStarters(){
//	    scoreStarters = new ArrayList<Thread>();
//	    for(IScorer score : scores){
//            ScoreStarter scoreStarter = new ScoreStarter(score,configPath);
//            scoreStarters.add(scoreStarter);
//        }
//	}
	private boolean initConf(String configPath){
        try{
            conf = Resources.getResourceAsProperties(configPath);
            
            cyctime = Long.valueOf(conf.getProperty("scoreCycleTime"));
            
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
    }
	
	@Override
	public void run(){
//		initScoreStarters();
//		if(scoreStarters != null){
//			for(Thread scoreStarter : scoreStarters){
//			    scoreStarter.start();
//			}
//		}
		
		try{
//            for(Thread scoreStarter : scoreStarters){
//                scoreStarter.join();
//            }
		    if(!initConf(configPath)){
		        return;
		    }
		    if(timer == null){
                timer = new SimpleTimer();
                timer.setAlertTime(System.currentTimeMillis());
            }
		    while(true){
    		    if(timer.isAlert() <= 0){
    		        logger.info("isAlert:"+timer.isAlert());
    		        for(IScorer score:scores){
    	                score.start(configPath);
    	            }
                    
                    timer.setAlertTime(System.currentTimeMillis()+cyctime);
                    
    		    }
    		    Thread.sleep(100);
		    }
        }catch(Exception ex){
            logger.error("error message",ex);
        }
	}
}
