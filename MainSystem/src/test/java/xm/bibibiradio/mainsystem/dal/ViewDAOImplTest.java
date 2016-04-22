package xm.bibibiradio.mainsystem.dal;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;

public class ViewDAOImplTest {
	private ViewDAO testDAO;
	@Before
	public void setUp() throws Exception {
		if(testDAO == null){
			testDAO = (ViewDAO)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("viewDAO");
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInsertView() {
		ViewData vd = new ViewData();
		vd.setFloor(1);
		vd.setGmtInsertTime(new Date());
		vd.setGmtViewTime(new Date());
		vd.setReplyCount(1);
		vd.setResourceId(1);
		vd.setViewContent("123");
		vd.setViewerName("a");
		vd.setViewerSource(1);
		
		testDAO.insertView(vd);
	}

}
