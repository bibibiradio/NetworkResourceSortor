package xm.bibibiradio.mainsystem.aop;

import org.apache.log4j.Logger;

public class AopTest {
	private static Logger logger = Logger.getLogger(AopTest.class);
	public void testAop(String testStr){
		logger.info("test aop start "+testStr);
	}
}
