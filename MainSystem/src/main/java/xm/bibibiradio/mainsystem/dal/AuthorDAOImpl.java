package xm.bibibiradio.mainsystem.dal;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class AuthorDAOImpl implements AuthorDAO {
    private SqlSessionFactory sqlSessionFactory;
    
    @Override
    public AuthorData select(String authorName, String authorType) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("authorName", authorName);
        params.put("authorType", authorType);
        
        try {
            return (AuthorData)session.selectOne("xm.bibibiradio.mainsystem.dal.AuthorDAO.select", params);
        } finally {
            session.close();
        }
    }

    @Override
    public long insert(AuthorData authorData) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        try {
            session.insert("xm.bibibiradio.mainsystem.dal.AuthorDAO.insert", authorData);
            return authorData.getAuthorId();
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
