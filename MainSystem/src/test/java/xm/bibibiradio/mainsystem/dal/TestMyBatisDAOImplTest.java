package xm.bibibiradio.mainsystem.dal;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import xm.bibibiradio.mainsystem.beanfactory.MainSystemBeanFactory;

public class TestMyBatisDAOImplTest {
    private TestIbatisDAO testIbatisDAO = null;
    @Before
    public void setUp() throws Exception {
        if(testIbatisDAO == null){
            testIbatisDAO = (TestIbatisDAO) MainSystemBeanFactory.getMainSystemBeanFactory().getBean("testMyBatisDAO");
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInsertObject() {
        TestIbatisData testData = new TestIbatisData();
        testData.setKey("a");
        testData.setIntKey(1);
        testData.setValue("2");
        testIbatisDAO.insertObject(testData);
        
        testData.setKey("b");
        testData.setIntKey(2);
        testData.setValue("3");
        long id = testIbatisDAO.insertObject(testData);
        System.out.println(id);
        assertTrue(id>1);
        
        
        testData.setKey("c");
        testData.setIntKey(3);
        testData.setValue("4");
        testIbatisDAO.insertObject(testData);
        
        assertTrue(true);
    }

    @Test
    public void testQueryObject() {
        TestIbatisData testData = testIbatisDAO.queryObject(2);
        assertTrue(testData!=null);
        assertTrue(testData.getKey().equals("b"));
        assertTrue(testData.getValue().equals("3"));
        
        testData = testIbatisDAO.queryObject(10);
        assertTrue(testData == null);
    }

    @Test
    public void testQueryList() {
        assertTrue(true);
    }
    
    @Test
    public void testQueryListByIntKey(){
        List<TestIbatisData> testDatas = testIbatisDAO.queryListByIntKey(1, 3);
        assertTrue(testDatas.size()==2);
    }

    @Test
    public void testUpdateObject() {
        //fail("Not yet implemented");
        TestIbatisData testData = new TestIbatisData();
        testData.setKey("abbbbb");
        testData.setIntKey(1);
        testData.setValue("222222");
        testIbatisDAO.updateObject(testData);
        
        testData = null;
        testData = testIbatisDAO.queryObject(1);
        assertTrue(testData!=null);
        assertTrue(testData.getKey().equals("abbbbb"));
        assertTrue(testData.getValue().equals("222222"));
    }

    @Test
    public void testDeleteAll() {
        //fail("Not yet implemented");
        testIbatisDAO.deleteAll();
    }

}
