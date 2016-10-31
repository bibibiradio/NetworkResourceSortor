package xm.bibibiradio.mainsystem.webservice.biz;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;

public class ResourceScoreBizTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        ResourceScoreBiz rsb = (ResourceScoreBiz)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("resourceScoreBiz");
        
    }

}
