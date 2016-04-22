package com.bibibiradio.executetesttime;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExecuteTimeTimerMngImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void test() {
        Properties testConfig = new Properties();
        testConfig.put("rollTime", "20000");
        testConfig.put("outputFilePath", "ExecuteTimeTest");
        testConfig.put("callBack", "com.bibibiradio.executetesttime.TestCallback");
        ExecuteTimeTimerMngImpl.setPropertyConfig(testConfig);
        
        ExecuteTimeTimerMngImpl testImpl = (ExecuteTimeTimerMngImpl) ExecuteTimeTimerMngImpl.GetExecuteTimeTimerMng();
        assertTrue(testImpl != null);
        
        assertTrue(testImpl.getRollTime() == 20000);
        
        assertTrue(testImpl.getUserCallback() instanceof TestCallback);
        TestCallback testCallback = (TestCallback) testImpl.getUserCallback();
        assertTrue(testCallback.getIsInit() == 1);
        
//        File outputFile = new File("ExecuteTimeTest_"+Thread.currentThread().getId());
//        assertTrue(outputFile.exists() && outputFile.isFile());
        
        for(int i=0;i<7;i++){
            testImpl.start("A");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            testImpl.end("A");
        } 
        
        for(int i=0;i<7;i++){
            testImpl.start("B");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            testImpl.end("B");
        }
        
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        testImpl.start("C");
        testImpl.end("C");
        
        assertTrue(testCallback.getNumOfResults() == 3);
        
        //outputFile.delete();
    }
    
}
