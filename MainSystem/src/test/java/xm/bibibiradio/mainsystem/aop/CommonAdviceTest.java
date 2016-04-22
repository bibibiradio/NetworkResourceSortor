package xm.bibibiradio.mainsystem.aop;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;

public class CommonAdviceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		AopTest aopTest = (AopTest) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("aopTestBean");
		aopTest.testAop("hello world");
		assertTrue(true);
	}

}
