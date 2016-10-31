package xm.bibibiradio.mainsystem.dal;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;
import xm.bibibiradio.mainsystem.starter.Env;

public class ResourceDAOImplTest {
	private ResourceDAO testDAO;
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test(){
	    Env.setEnvFlag(1);
	    if(testDAO == null){
            testDAO = (ResourceDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("resourceDAO");
        }
	    HashMap<String,Object> r = testDAO.selectCommentByRid(1);
	    long r_comment = (Long)r.get("r_comment");
	    long r_inner_id = (Long)r.get("r_inner_id");
	    System.out.println(r_comment);
	    System.out.println(r_inner_id);
	    
	    List<Long> r2 = testDAO.selectDateSiteList(new Date(System.currentTimeMillis() - 50*24*60*60*1000L), new Date(), 1);
	    for(Long l : r2){
	        System.out.println(l);
	    }
	    
	}
	

}
