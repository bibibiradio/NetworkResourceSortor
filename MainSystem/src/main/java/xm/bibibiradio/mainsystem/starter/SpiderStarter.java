package xm.bibibiradio.mainsystem.starter;

import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.spider.ISpider;

public class SpiderStarter extends Thread {
    private ISpider spider;
    private String configPath;
    private Properties conf;
    private boolean isRun = false;
    private long lastStartTime;
    private static Logger logger = Logger.getLogger(SpiderStarter.class);
    
    public SpiderStarter(ISpider spider,String configPath){
        this.spider = spider;
        this.configPath = configPath;
    }

    public ISpider getSpider() {
        return spider;
    }

    public void setSpider(ISpider spider) {
        this.spider = spider;
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
            lastStartTime = System.currentTimeMillis();
            if(conf == null){
                conf = Resources.getResourceAsProperties(configPath);                
            }
            if(spider != null){
                isRun = true;
                spider.start(configPath);
                lastStartTime = System.currentTimeMillis();
                isRun = false;
            }
        }catch(Exception ex){
            logger.error("error message",ex);
        }
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    public long getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(long lastStartTime) {
        this.lastStartTime = lastStartTime;
    }
    
    
}
