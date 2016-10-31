package xm.bibibiradio.mainsystem.beanfactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xm.bibibiradio.mainsystem.starter.Env;

public class MainSystemBeanFactory {
	private static MainSystemBeanFactory mainSystemBeanFactory = null;
	private static String beanConfigPath = "classpath:beanConfig.xml";
	private BeanFactory beanFactory = null;
	
	private MainSystemBeanFactory(){
		/**
		 * Create Old bean factory
		 */
//		beanFactory = new DefaultListableBeanFactory();
//		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
//		reader.loadBeanDefinitions(beanConfigPath);
		
		/**
		 * Create ApplicationContext,support aop
		 */
		if(Env.getEnvFlag() == 0){
			beanConfigPath = "classpath:beanConfig.xml";
		}else{
			beanConfigPath = "classpath:beanConfigDev.xml";
		}
		beanFactory = new ClassPathXmlApplicationContext(beanConfigPath);
	}
	
	public static MainSystemBeanFactory getMainSystemBeanFactory(){
		if(mainSystemBeanFactory == null){
		    synchronized (MainSystemBeanFactory.class){
		        if(mainSystemBeanFactory == null)
		            mainSystemBeanFactory = new MainSystemBeanFactory();
		    }
		}
		return mainSystemBeanFactory;
	}
	
	public Object getBean(String beanName){
		return beanFactory.getBean(beanName);
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public static void setMainSystemBeanFactory(
			MainSystemBeanFactory mainSystemBeanFactory) {
		MainSystemBeanFactory.mainSystemBeanFactory = mainSystemBeanFactory;
	}
	
	
}
