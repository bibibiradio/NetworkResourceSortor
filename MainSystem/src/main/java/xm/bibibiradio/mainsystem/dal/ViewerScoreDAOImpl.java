package xm.bibibiradio.mainsystem.dal;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ViewerScoreDAOImpl implements ViewerScoreDAO {
    private SqlSessionFactory sqlSessionFactory;
    
    @Override
    public long insert(ViewerScoreData viewerScoreData) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        try {
            return session.insert("xm.bibibiradio.mainsystem.dal.ViewerScoreDAO.insert", viewerScoreData);
        } finally {
            session.close();
        }
        
    }

    @Override
    public ViewerScoreData select(String viewerName,String viewerType) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("viewerName", viewerName);
        params.put("viewerType", viewerType);
        
        try {
            return (ViewerScoreData)session.selectOne("xm.bibibiradio.mainsystem.dal.ViewerScoreDAO.select", params);
        } finally {
            session.close();
        }
    }

    @Override
    public void update(String viewerName, String viewerType,long score) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("viewerName", viewerName);
        params.put("viewerType", viewerType);
        params.put("score", score);
        
        try {
            session.update("xm.bibibiradio.mainsystem.dal.ViewerScoreDAO.select", params);
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
