package xm.bibibiradio.mainsystem.webservice.dal;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.webservice.biz.ResourceScoreData;

public class ResourceDAOImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        ResourceDAO rd = (ResourceDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("resourceDAO");
        List<ResourceScoreData> datas = rd.selectResourceListOrderScore(0, 1, "游戏", new Date(System.currentTimeMillis()-(30*24*60*60*1000L)), new Date(), 1, 100);
        for(ResourceScoreData data:datas){
            System.out.println(data.getTitle());
            System.out.println(data.getAuthorName());
            System.out.println(data.getResourceUrl());
        }
    }

}
