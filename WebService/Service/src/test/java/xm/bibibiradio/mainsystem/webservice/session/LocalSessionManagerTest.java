package xm.bibibiradio.mainsystem.webservice.session;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import xm.bibibiradio.mainsystem.webservice.common.session.LocalSessionManager;

public class LocalSessionManagerTest {
    final static Logger logger = Logger.getLogger(LocalSessionManagerTest.class);
    @Test
    public void test() {
        LocalSessionManager sessions = new LocalSessionManager();
        
        Map<Object,Object> amap;
        amap = sessions.getSession("1");
        assertTrue(amap == null);
        
        String key1 = sessions.genSessionKey();
        logger.info(key1);
        //System.out.println(key1);
        
        amap = new HashMap();
        amap.put("1", "2222");
        
        sessions.setSession(key1, amap);
        
        amap = sessions.getSession(key1);
        
        assertTrue(amap != null);
        assertTrue(amap.get("1").equals("2222"));
        
        sessions.setSession("123", amap);
        amap = sessions.getSession("123");
        assertTrue(amap == null);
        
        //write expire test
        sessions = new LocalSessionManager(4,5000,50000,"123456");
        key1 = sessions.genSessionKey();
        amap = new HashMap();
        amap.put("1", "2222");
        sessions.setSession(key1, amap);
        
        amap = sessions.getSession(key1);
        assertTrue(amap!=null);
        assertTrue(amap.get("1").equals("2222"));
        
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("error message",e);
        }
        
        amap = sessions.getSession(key1);
        assertTrue(amap == null);
        
        //access expire test
        //first can expire
        sessions = new LocalSessionManager(4,50000,5000,"123456");
        key1 = sessions.genSessionKey();
        amap = new HashMap();
        amap.put("1", "2222");
        sessions.setSession(key1, amap);
        
        amap = sessions.getSession(key1);
        assertTrue(amap!=null);
        assertTrue(amap.get("1").equals("2222"));
        
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("error message",e);
        }
        
        amap = sessions.getSession(key1);
        assertTrue(amap == null);
        
        //sec get can update time
        key1 = sessions.genSessionKey();
        amap = new HashMap();
        amap.put("1", "2222");
        sessions.setSession(key1, amap);
        
        amap = sessions.getSession(key1);
        assertTrue(amap!=null);
        assertTrue(amap.get("1").equals("2222"));
        
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("error message",e);
        }
        
        amap = sessions.getSession(key1);
        assertTrue(amap!=null);
        assertTrue(amap.get("1").equals("2222"));
        
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("error message",e);
        }
        
        amap = sessions.getSession(key1);
        assertTrue(amap!=null);
        assertTrue(amap.get("1").equals("2222"));
        
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error("error message",e);
        }
        
        amap = sessions.getSession(key1);
        assertTrue(amap==null);
        
        key1 = sessions.genSessionKey();
        amap = new HashMap();
        amap.put("aa", "1234");
        sessions.setSession(key1, amap);
        
        amap = sessions.getSession(key1);
        amap.put("aa", "4321");
        
        amap = sessions.getSession(key1);
        assertTrue(amap!=null);
        assertTrue(amap.get("aa").equals("4321"));
    }

}
