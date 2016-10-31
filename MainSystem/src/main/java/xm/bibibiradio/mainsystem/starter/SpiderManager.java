package xm.bibibiradio.mainsystem.starter;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.log4j.Logger;

import xm.bibibiradio.mainsystem.spider.ISpider;

public class SpiderManager extends Thread {
    private static Logger                   LOGGER                = Logger
                                                                      .getLogger(SpiderManager.class);
    private ArrayList<ISpider>              spiders               = null;
    private ArrayList<SpiderForwardStarter> spiderForwardStarters = null;
    private ArrayList<SpiderUpdateStarter>  spiderUpdateStarters  = null;
    private ArrayList<SpiderAfterStarter>   spiderAfterStarters   = null;
    private String                          configPath            = null;
    private Properties                      conf                  = null;

    private long                            rerunSpiderTime;
    private long                            rerunSpiderUpdateTime;

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

    public ArrayList<SpiderForwardStarter> getSpiderForwardStarters() {
        return spiderForwardStarters;
    }

    public void setSpiderForwardStarters(ArrayList<SpiderForwardStarter> spiderForwardStarters) {
        this.spiderForwardStarters = spiderForwardStarters;
    }

    public ArrayList<SpiderAfterStarter> getSpiderAfterStarters() {
        return spiderAfterStarters;
    }

    public void setSpiderAfterStarters(ArrayList<SpiderAfterStarter> spiderAfterStarters) {
        this.spiderAfterStarters = spiderAfterStarters;
    }

    public Properties getConf() {
        return conf;
    }

    public void setConf(Properties conf) {
        this.conf = conf;
    }

    public long getRerunSpiderTime() {
        return rerunSpiderTime;
    }

    public void setRerunSpiderTime(long rerunSpiderTime) {
        this.rerunSpiderTime = rerunSpiderTime;
    }

    public long getRerunSpiderUpdateTime() {
        return rerunSpiderUpdateTime;
    }

    public void setRerunSpiderUpdateTime(long rerunSpiderUpdateTime) {
        this.rerunSpiderUpdateTime = rerunSpiderUpdateTime;
    }

    public ArrayList<SpiderUpdateStarter> getSpiderUpdateStarters() {
        return spiderUpdateStarters;
    }

    public void setSpiderUpdateStarters(ArrayList<SpiderUpdateStarter> spiderUpdateStarters) {
        this.spiderUpdateStarters = spiderUpdateStarters;
    }

    private void initSpider() throws Exception {
        spiderForwardStarters = new ArrayList<SpiderForwardStarter>();
        spiderUpdateStarters = new ArrayList<SpiderUpdateStarter>();
        spiderAfterStarters = new ArrayList<SpiderAfterStarter>();
        for (ISpider spider : spiders) {
            spider.init(configPath);
            SpiderForwardStarter spiderForwardStarter = new SpiderForwardStarter(spider, configPath);
            SpiderUpdateStarter spiderUpdateStarter = new SpiderUpdateStarter(spider, configPath);
            SpiderAfterStarter spiderAfterStarter = new SpiderAfterStarter(spider, configPath);
            spiderForwardStarters.add(spiderForwardStarter);
            spiderUpdateStarters.add(spiderUpdateStarter);
            spiderAfterStarters.add(spiderAfterStarter);
        }
    }

    private boolean initConf(String configPath) {
        try {
            conf = Resources.getResourceAsProperties(configPath);
            rerunSpiderTime = Long.valueOf(conf.getProperty("rerunSpiderTime"));
            rerunSpiderUpdateTime = Long.valueOf(conf.getProperty("rerunSpiderUpdateTime"));

            return true;
        } catch (Exception ex) {
            LOGGER.error("error message", ex);
            return false;
        }
    }

    @Override
    public void run() {
        try {
            initSpider();
            initConf(configPath);
        } catch (Exception ex) {
            LOGGER.error("error", ex);
        }
        while (true) {
            try {

                if (spiderForwardStarters != null) {
                    for (SpiderForwardStarter spiderForwardStarter : spiderForwardStarters) {
                        if (!spiderForwardStarter.isRun()
                            && System.currentTimeMillis() - spiderForwardStarter.getLastStartTime() > rerunSpiderTime) {
                            //spiderStarter.start();
                            new Thread(spiderForwardStarter).start();
                        }
                    }
                }

                if (spiderUpdateStarters != null) {
                    for (SpiderUpdateStarter spiderUpdateStarter : spiderUpdateStarters) {
                        if (!spiderUpdateStarter.isRun()
                            && System.currentTimeMillis() - spiderUpdateStarter.getLastStartTime() > rerunSpiderUpdateTime) {
                            //spiderUpdateStarter.start();
                            new Thread(spiderUpdateStarter).start();
                        }
                    }
                }

                if (spiderAfterStarters != null) {
                    for (SpiderAfterStarter spiderAfterStarter : spiderAfterStarters) {
                        if (!spiderAfterStarter.isRun()
                            && System.currentTimeMillis() - spiderAfterStarter.getLastStartTime() > rerunSpiderTime) {
                            //spiderStarter.start();
                            new Thread(spiderAfterStarter).start();
                        }
                    }
                }

                Thread.sleep(1000);
            } catch (Exception ex) {
                LOGGER.error("error message", ex);
            }
        }
    }
}
