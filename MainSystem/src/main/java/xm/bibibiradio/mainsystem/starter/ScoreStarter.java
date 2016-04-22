package xm.bibibiradio.mainsystem.starter;

import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.score.IScorer;

public class ScoreStarter extends Thread {
    private IScorer score;
    private String configPath;
    private static Properties conf;
    private static Logger logger = Logger.getLogger(ScoreStarter.class);
    
    public ScoreStarter(IScorer score,String configPath){
        this.score = score;
        this.configPath = configPath;
    }

    public IScorer getScore() {
        return score;
    }

    public void setScore(IScorer score) {
        this.score = score;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
    
    @Override
    public void run(){
        try{
            if(conf == null){
                conf = Resources.getResourceAsProperties(configPath);                
            }
            if(score != null){
                score.start(configPath);
            }
        }catch(Exception ex){
            logger.error("error message",ex);
        }
    }
}
