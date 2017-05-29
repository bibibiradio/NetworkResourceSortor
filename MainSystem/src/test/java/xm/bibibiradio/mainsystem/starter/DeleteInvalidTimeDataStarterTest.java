package xm.bibibiradio.mainsystem.starter;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;

public class DeleteInvalidTimeDataStarterTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        Env.setEnvFlag(1);
        
        DeleteInvalidTimeDataStarter di = (DeleteInvalidTimeDataStarter)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("deleteInvalidTimeDataStarter");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date lastStoreTime = df2.parse("2016-07-30 11:11:00");
            lastStoreTime = new Date(System.currentTimeMillis()-15552000000L);
            di.delete(lastStoreTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        di = (DeleteInvalidTimeDataStarter)MainSystemBeanFactory.getMainSystemBeanFactory().getBean("deleteInvalidTimeDataStarter");
        di.start();
        
        
    }

}
