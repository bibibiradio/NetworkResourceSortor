package xm.bibibiradio.mainsystem.util;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class HiveShellClientTest {
    final static private Logger logger = Logger.getLogger(HiveShellClientTest.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        String ret = HiveShellClient.retStringShellExec("ps -ef");
        logger.info(ret);
        assertTrue(ret != null);
        
        List<String> ret2 = HiveShellClient.retLineStringShellExec("ps -ef");
        assertTrue(ret2 != null);
        for(String line : ret2){
            logger.info(line);
        }
    }
    
    @Test
    public void test2() {
        
        
        List<String> ret2 = HiveShellClient.retLineStringHiveSql("Select max(r_gmt_create) from resources;");
        assertTrue(ret2 != null);
        for(String line : ret2){
            logger.info(line);
        }
    }

}
