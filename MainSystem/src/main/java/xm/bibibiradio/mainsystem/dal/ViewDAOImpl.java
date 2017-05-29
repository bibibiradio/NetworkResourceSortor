package xm.bibibiradio.mainsystem.dal;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ViewDAOImpl implements ViewDAO {
	private SqlSessionFactory sqlSessionFactory;
	@Override
	public long insertView(ViewData viewData) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();
        
        try {
            return session.insert("xm.bibibiradio.mainsystem.dal.ViewDAO.insertView", viewData);
        } finally {
            session.close();
        }
	}
	
	@Override
    public ViewData selectMaxFloor(long resourceId) {
        // TODO Auto-generated method stub
	    SqlSession session = sqlSessionFactory.openSession();
        ViewData result = null;
        try {
            result = session.selectOne("xm.bibibiradio.mainsystem.dal.ViewDAO.selectMaxFloor", resourceId);
        } finally {
            session.close();
        }
        return result;
    }
	
	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();
        
        try {
            session.delete("xm.bibibiradio.mainsystem.dal.ViewDAO.deleteAll");
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

    @Override
    public void deleteRid(long rId) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        try {
            session.delete("xm.bibibiradio.mainsystem.dal.ViewDAO.deleteRid",rId);
        } finally {
            session.close();
        }  
    }

}
