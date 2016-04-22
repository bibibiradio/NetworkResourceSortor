package xm.bibibiradio.mainsystem.dal;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ViewerDAOImpl implements ViewerDAO {
    private SqlSessionFactory sqlSessionFactory;
    
    @Override
    public ViewerData select(String viewerName, String viewerType) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("viewerName", viewerName);
        params.put("viewerType", viewerType);
        
        try {
            return (ViewerData)session.selectOne("xm.bibibiradio.mainsystem.dal.ViewerDAO.select", params);
        } finally {
            session.close();
        }
        
    }

    @Override
    public long insert(ViewerData viewerData) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        try {
            session.insert("xm.bibibiradio.mainsystem.dal.ViewerDAO.insert", viewerData);
            return viewerData.getViewerId();
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
