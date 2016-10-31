package xm.bibibiradio.post.biz;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.post.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.post.util.MailSender;
import xm.bibibiradio.post.util.MailTemplate;

public class UserPostResourceBizTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        UserPostResourceBiz rd = (UserPostResourceBiz)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("userPostResourceBiz");
        try{
            List<PostConfigData> lists = rd.getAllPostTasks();
            for(PostConfigData item : lists){
                System.out.println(item.getPostAddress());
                System.out.println(item.getPostCategory());
                System.out.println(item.getPostTags());
            }
            
            PostConfigData p = new PostConfigData();
            p.setLastPost(new Date(System.currentTimeMillis() - 1*24*60*60*1000L));
            p.setPostAddress("qbjxiaolei@163.com");
            p.setPostCategory("游戏");
            p.setPostFreq(1*12*60*60*1000L);
            p.setPostLimitDay(365);
            p.setPostNum(10);
            p.setPostSite(1);
            p.setPostTags("单机联机");
            p.setPostType(0);
            p.setPostWay(0);
            p.setUid(1);
            
            List<PostResourceData> r = rd.getNeedPostResource(lists.get(0));
            for(PostResourceData item : r){
                System.out.println(item.getTitle());
            }
            
            MailSender mailSender = new MailSender();
            mailSender.init("smtp.163.com", "25", false, true, "qbjxiaolei@163.com", "Aa33333586");

            try {
                mailSender.sendMail("qbjxiaolei@163.com", "qbjxiaolei@163.com", null, "test_xiaolei", MailTemplate.getResourcesContent(r));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            rd.updatePostTime(lists.get(0).getPostConfigId());
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

}
