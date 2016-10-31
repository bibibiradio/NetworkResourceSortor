package xm.bibibiradio.post.beanfactory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MainSystemBeanFactory {
    private static MainSystemBeanFactory mainSystemBeanFactory = null;
    private static String beanConfigPath = "classpath:beanConfig.xml";
    private BeanFactory beanFactory = null;
    
    private MainSystemBeanFactory(){
        /**
         * Create Old bean factory
         */
//      beanFactory = new DefaultListableBeanFactory();
//      XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) beanFactory);
//      reader.loadBeanDefinitions(beanConfigPath);
        
        /**
         * Create ApplicationContext,support aop
         */
        
        beanConfigPath = "classpath:beanConfig.xml";
        
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
