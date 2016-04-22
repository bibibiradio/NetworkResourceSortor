package xm.bibibiradio.mainsystem.webservice.dal;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import xm.bibibiradio.mainsystem.webservice.common.util.CachedAbstract;
import xm.bibibiradio.mainsystem.webservice.dal.dataobject.MostPvResource;

public class ResourceDAOImpl extends CachedAbstract implements ResourceDAO {
    private SqlSessionFactory sqlSessionFactory;
    
    public ResourceDAOImpl(String accessExpire, String maxNum, String threadNum) {
        super(accessExpire, maxNum, threadNum);
    }
    
    @Override
    public List<String> selectCategories() {
        // TODO Auto-generated method stub
        List<String> result;
        result = (List<String>) get("selectCategories");
        if(result != null){
            return result;
        }
        
        SqlSession session = sqlSessionFactory.openSession();
        
        //ResourceData result = null;
        try {
            result = session.selectList("xm.bibibiradio.mainsystem.webservice.dal.ResourceDAO.selectCategories");
            put("selectCategories",result);
            return result;
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<MostPvResource> selectPvResource(Map<String,Object> params) {
        // TODO Auto-generated method stub
        List<MostPvResource> result;
        result = (List<MostPvResource>) get("selectPvResource:"+JSONObject.fromObject(params).toString());
        if(result != null){
            return result;
        }
        
        SqlSession session = sqlSessionFactory.openSession();
        
        //ResourceData result = null;
        try {
            result = session.selectList("xm.bibibiradio.mainsystem.webservice.dal.ResourceDAO.selectPvResource",params);
            put("selectPvResource:"+JSONObject.fromObject(params).toString(),result);
            return result;
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
