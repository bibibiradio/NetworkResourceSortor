package xm.bibibiradio.mainsystem.dal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class TestMyBatisDAOImpl implements TestIbatisDAO {
    private SqlSessionFactory sqlSessionFactory;
    
    @Override
    public TestIbatisData queryObject(long intKey) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        TestIbatisData result = null;
        try {
            result = (TestIbatisData) session.selectOne("xm.bibibiradio.mainsystem.dal.TestIbatisDAO.queryObject", intKey);
        } finally {
            session.close();
        }
        
        return result;
    }

    @Override
    public List<TestIbatisData> queryList(long minIntId, long maxIntId) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        Map<String,Long> params = new HashMap<String,Long>();
        params.put("minIntId", minIntId);
        params.put("maxIntId", maxIntId);
        
        List<TestIbatisData> result = null;
        try {
            result = session.selectList("xm.bibibiradio.mainsystem.dal.TestIbatisDAO.queryList", params);
        } finally {
            session.close();
        }
        
        return result;
    }

    @Override
    public List<TestIbatisData> queryListByIntKey(long minIntKey, long maxIntKey) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        Map<String,Long> params = new HashMap<String,Long>();
        params.put("minIntKey", minIntKey);
        params.put("maxIntKey", maxIntKey);
        
        List<TestIbatisData> result = null;
        try {
            result = session.selectList("xm.bibibiradio.mainsystem.dal.TestIbatisDAO.queryListByIntKey", params);
        } finally {
            session.close();
        }
        
        return result;
    }

    @Override
    public long insertObject(TestIbatisData testIbatisData) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        try {
            session.insert("xm.bibibiradio.mainsystem.dal.TestIbatisDAO.insertObject", testIbatisData);
            return testIbatisData.getIbatisId();
        } finally {
            session.close();
        }      
    }

    @Override
    public void updateObject(TestIbatisData testIbatisData) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        try {
            session.update("xm.bibibiradio.mainsystem.dal.TestIbatisDAO.updateObject", testIbatisData);
        } finally {
            session.close();
        }
        
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        try {
            session.delete("xm.bibibiradio.mainsystem.dal.TestIbatisDAO.deleteAll");
        } finally {
            session.close();
        }
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    
}
