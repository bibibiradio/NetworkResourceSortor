package xm.bibibiradio.mainsystem.webservice.dal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;


public class ResourceDAOImpl implements ResourceDAO {
    private SqlSessionFactory sqlSessionFactory;
    
    @SuppressWarnings("unchecked")
    @Override
    public List selectResourceListOrderScore(int rType, int rSite, String rCategory,
                                                          Date rGmtCreateStart, Date rGmtCreateEnd,
                                                          long pageStart, long pageEnd) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("rType", rType);
        params.put("rSite", rSite);
        params.put("rCategory", rCategory);
        params.put("rGmtCreateStart", rGmtCreateStart);
        params.put("rGmtCreateEnd", rGmtCreateEnd);
        params.put("pageStart", pageStart);
        params.put("pageEnd", pageEnd);
        
        List result = null;
        try {
            result = (List) session.selectList("xm.bibibiradio.mainsystem.webservice.dal.ResourceDAO.selectResourceListOrderScore", params);
        } finally {
            session.close();
        }
           
       return result;
    }
    
    @Override
    public long selectResourceNum(int rType, int rSite, String rCategory, Date rGmtCreateStart,
                                  Date rGmtCreateEnd) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("rType", rType);
        params.put("rSite", rSite);
        params.put("rCategory", rCategory);
        params.put("rGmtCreateStart", rGmtCreateStart);
        params.put("rGmtCreateEnd", rGmtCreateEnd);
        
        Long result = null;
        try {
            result = (Long) session.selectOne("xm.bibibiradio.mainsystem.webservice.dal.ResourceDAO.selectResourceNum", params);
        } finally {
            session.close();
        }
        
        if(result == null)
            result = Long.valueOf(0);
        
        return result;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    
    
    

}
