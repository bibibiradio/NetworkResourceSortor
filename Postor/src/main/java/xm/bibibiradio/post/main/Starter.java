package xm.bibibiradio.post.main;

import java.util.List;

import org.apache.log4j.Logger;

import xm.bibibiradio.post.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.post.biz.PostConfigData;
import xm.bibibiradio.post.biz.PostResourceData;
import xm.bibibiradio.post.biz.UserPostResourceBiz;
import xm.bibibiradio.post.util.MailSender;
import xm.bibibiradio.post.util.ContentTemplate;

public class Starter {
    final static private Logger LOGGER = Logger.getLogger(Starter.class);
    private long lastCycTime = 0;
    private MailSender mailSender;
    
    public void start(){
        try{
            mailSender = new MailSender();
            mailSender.init("smtp.163.com", "25", false, true, "qbjxiaolei@163.com", "Aa33333586");
            UserPostResourceBiz userPostResourceBiz = (UserPostResourceBiz)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("userPostResourceBiz");
            while(true){
                if(System.currentTimeMillis() - lastCycTime >= 5*60*1000L){
                    try{
                        List<PostConfigData> postConfigs = userPostResourceBiz.getAllPostTasks();
                        for(PostConfigData postConfig:postConfigs){
                            int postWay = postConfig.getPostWay();
                            String postAddress = postConfig.getPostAddress();
                            
                            List<PostResourceData> postResourceDatas = userPostResourceBiz.getNeedPostResource(postConfig);
                            if(postResourceDatas == null || postResourceDatas.size() == 0)
                                continue;
                            
                            String content = ContentTemplate.getResourcesContent(postResourceDatas,postConfig.getPostType());
                            if(postWay == 0){
                                mailSender.sendMail("qbjxiaolei@163.com", postAddress, null, postConfig.getPostTitle(), content);
                                userPostResourceBiz.updatePostTime(postConfig.getPostConfigId());
                            }
                        }
                    }catch(Exception ex){
                        LOGGER.error("error",ex);
                    }finally{
                        lastCycTime = System.currentTimeMillis();
                    }
                }
                Thread.sleep(1000);
            }
        }catch(Exception ex){
            LOGGER.error("error",ex);
        }
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Starter starter = new Starter();
        starter.start();
        
    }

}
