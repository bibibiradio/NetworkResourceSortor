package xm.bibibiradio.post.dal;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.post.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.post.biz.PostConfigData;



public class PostConfigDAOImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        PostConfigDAO rd = (PostConfigDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("postConfigDAO");
        
        List<PostConfigData> lists = rd.selectAllPostConfigData();
        for(PostConfigData item : lists){
            System.out.println(item.getPostAddress());
        }
    }

}
