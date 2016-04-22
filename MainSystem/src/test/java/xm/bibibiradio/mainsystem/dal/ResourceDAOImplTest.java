package xm.bibibiradio.mainsystem.dal;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;

public class ResourceDAOImplTest {
	private ResourceDAO testDAO;
	@Before
	public void setUp() throws Exception {
		if(testDAO == null){
			testDAO = (ResourceDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("resourceDAO");
		}
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testInsertResource() {
		ResourceData rData = new ResourceData();
		rData.setrType("1");
		rData.setAuthor("xl");
		rData.setrGmtCreate(new Date());
		rData.setrUrl("111111");
		rData.setrCategory("123");
		testDAO.insertResource(rData);
		
		rData = new ResourceData();
		rData.setrType("1");
		rData.setAuthor("xl2");
		rData.setrGmtCreate(new Date());
		rData.setrUrl("111111");
		rData.setrCategory("123");
		
		testDAO.insertResource(rData);
		
	}
	
	@Test
	public void testSelectMaxDataByType() {
		ResourceData rData = testDAO.selectMaxDataByType("221");
		assertTrue(rData!=null);
		assertTrue(rData.getrId()>1);
		
		rData = testDAO.selectMaxDataByType("aaa");
		assertTrue(rData==null);
		//assertTrue(rData.getrId()>1);
		
		rData = testDAO.selectMaxDataByType("112");
		System.out.println(rData.getrTitle());
	}
	

}
