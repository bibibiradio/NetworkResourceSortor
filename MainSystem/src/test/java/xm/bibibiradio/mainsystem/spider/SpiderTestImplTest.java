package xm.bibibiradio.mainsystem.spider;

import static org.junit.Assert.*;

import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SpiderTestImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStart() {
		assertTrue(true);
		try{
		    String configPath = "testConf.properties";
		    Properties conf = Resources.getResourceAsProperties(configPath);
		    
		    assertTrue(conf.getProperty("name").equals("xiaolei.xl"));
		    assertTrue(conf.getProperty("password").equals("123456"));
		    assertTrue(conf.getProperty("no") == null);
		}catch(Exception ex){
		    assertTrue(false);
		}
	}

}
