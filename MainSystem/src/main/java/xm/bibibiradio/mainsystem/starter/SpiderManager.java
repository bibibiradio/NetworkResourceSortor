package xm.bibibiradio.mainsystem.starter;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.spider.ISpider;

public class SpiderManager extends Thread {
    private static Logger logger = Logger.getLogger(SpiderManager.class);
    private ArrayList<ISpider> spiders = null;
    private ArrayList<SpiderStarter> spiderStarters = null;
    private ArrayList<SpiderUpdateStarter> spiderUpdateStarters = null;
    private String configPath = null;
    private Properties conf = null;
    
    private long rerunSpiderTime;
    private long rerunSpiderUpdateTime;


    public ArrayList<ISpider> getSpiders() {
        return spiders;
    }


    public void setSpiders(ArrayList<ISpider> spiders) {
        this.spiders = spiders;
    }


    public String getConfigPath() {
        return configPath;
    }


    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    

    public ArrayList<SpiderStarter> getSpiderStarters() {
        return spiderStarters;
    }


    public void setSpiderStarters(ArrayList<SpiderStarter> spiderStarters) {
        this.spiderStarters = spiderStarters;
    }


    public ArrayList<SpiderUpdateStarter> getSpiderUpdateStarters() {
        return spiderUpdateStarters;
    }


    public void setSpiderUpdateStarters(ArrayList<SpiderUpdateStarter> spiderUpdateStarters) {
        this.spiderUpdateStarters = spiderUpdateStarters;
    }


    private void initSpiderStarters() {
        spiderStarters = new ArrayList<SpiderStarter>();
        spiderUpdateStarters = new ArrayList<SpiderUpdateStarter>();
        for (ISpider spider : spiders) {
            SpiderStarter spiderStarter = new SpiderStarter(spider, configPath);
            SpiderUpdateStarter spiderUpdateStarter = new SpiderUpdateStarter(spider, configPath);
            spiderStarters.add(spiderStarter);
            spiderUpdateStarters.add(spiderUpdateStarter);
        }
    }
    
    private boolean initConf(String configPath){
        try{
            conf = Resources.getResourceAsProperties(configPath);
            rerunSpiderTime = Long.valueOf(conf.getProperty("rerunSpiderTime"));
            rerunSpiderUpdateTime = Long.valueOf(conf.getProperty("rerunSpiderUpdateTime"));
            
            return true;
        }catch(Exception ex){
            logger.error("error message",ex);
            return false;
        }
    }

    @Override
    public void run() {
        initSpiderStarters();
        initConf(configPath);
        while(true){
            try {
//                if (spiderStarters != null) {
//                    for (SpiderStarter spiderStarter : spiderStarters) {
//                        spiderStarter.join(10);
//                    }
//                }
//                
//                if (spiderUpdateStarters != null) {
//                    for (SpiderUpdateStarter spiderUpdateStarter : spiderUpdateStarters) {
//                        spiderUpdateStarter.join(10);
//                    }
//                }
                
                if (spiderStarters != null) {
                    for (SpiderStarter spiderStarter : spiderStarters) {
                        if(!spiderStarter.isRun() && System.currentTimeMillis() - spiderStarter.getLastStartTime() > rerunSpiderTime){
                            //spiderStarter.start();
                            new Thread(spiderStarter).start();
                        }
                    }
                }
                
                if (spiderUpdateStarters != null) {
                    for (SpiderUpdateStarter spiderUpdateStarter : spiderUpdateStarters) {
                        if(!spiderUpdateStarter.isRun() && System.currentTimeMillis() - spiderUpdateStarter.getLastStartTime() > rerunSpiderUpdateTime){
                            //spiderUpdateStarter.start();
                            new Thread(spiderUpdateStarter).start();
                        }
                    }
                }
                
                Thread.sleep(1000);
            } catch (Exception ex) {
                logger.error("error message", ex);
            }
        }
    }
}
