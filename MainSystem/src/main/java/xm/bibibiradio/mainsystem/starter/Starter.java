package xm.bibibiradio.mainsystem.starter;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.bibibiradio.executetesttime.ExecuteTimeTimerThreadImpl;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;

public class Starter {
	private static Logger logger = Logger.getLogger(Starter.class);
	
	private static void initExecuteTestTime(){
		Properties prop = new Properties();
		prop.put("rollTime", "30000");
		prop.put("outputFilePath", "logs/ExecuteTimeTest");
		ExecuteTimeTimerThreadImpl.setPropertyConfig(prop);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//logger.info("Hello world");
		if(args.length == 0){
			Env.setEnvFlag(0);
		}else if(args[0].equals("dev") || args[0].equals("test")){
			Env.setEnvFlag(1);
		}else{
			Env.setEnvFlag(0);
		}
		
		initExecuteTestTime();
		
		SpiderManager spiderManager = (SpiderManager) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("spiderManager");
		ScoreManager scoreManager = (ScoreManager) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("scoreManager");
		
		spiderManager.start();
		scoreManager.start();
		
		try {
		    spiderManager.join();
			scoreManager.join();
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			logger.error("error message",ex);
		}
	}

}
