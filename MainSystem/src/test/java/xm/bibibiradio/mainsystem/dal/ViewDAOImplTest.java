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
		
	}

}
