package xm.bibibiradio.mainsystem.dal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ResourceDAOImpl implements ResourceDAO {
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
	public ResourceData selectMaxDataByType(String rType) {
		// TODO Auto-generated method stub
		 SqlSession session = sqlSessionFactory.openSession();
	        
		 ResourceData result = null;
	     try {
	         result = (ResourceData) session.selectOne("xm.bibibiradio.mainsystem.dal.ResourceDAO.selectMaxDataByType", rType);
	     } finally {
	         session.close();
	     }
	        
	    return result;
	}
	
	@Override
    public ResourceData selectDateTypeFirst(Date startDate, Date endDate, String rType) {
        // TODO Auto-generated method stub
	    SqlSession session = sqlSessionFactory.openSession();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("rType", rType);
        ResourceData result = null;
        try {
            result = (ResourceData) session.selectOne("xm.bibibiradio.mainsystem.dal.ResourceDAO.selectDateTypeFirst", params);
        } finally {
            session.close();
        }
           
       return result;
    }
	
	@Override
    public ResourceData selectDateTypeEnd(Date startDate, Date endDate, String rType) {
        // TODO Auto-generated method stub
	    SqlSession session = sqlSessionFactory.openSession();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("rType", rType);
        ResourceData result = null;
        try {
            result = (ResourceData) session.selectOne("xm.bibibiradio.mainsystem.dal.ResourceDAO.selectDateTypeEnd", params);
        } finally {
            session.close();
        }
           
       return result;
    }
	
	@Override
    public void updateResource(ResourceData resourceData) {
        // TODO Auto-generated method stub
	    SqlSession session = sqlSessionFactory.openSession();
	    
	    try {
            session.update("xm.bibibiradio.mainsystem.dal.ResourceDAO.updateResource", resourceData);
        } finally {
            session.close();
        }
    }

	@Override
	public long insertResource(ResourceData rData) {
		SqlSession session = sqlSessionFactory.openSession();
        
        try {
            return session.insert("xm.bibibiradio.mainsystem.dal.ResourceDAO.insertResource", rData);
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
