package xm.bibibiradio.mainsystem.webservice.dal;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import xm.bibibiradio.mainsystem.webservice.biz.AuthorScoreData;

public class AuthorDAOImpl implements AuthorDAO {
    private SqlSessionFactory sqlSessionFactory;
    
    

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<AuthorScoreData> selectAuthorListOrderScore(int site, String category,
                                                            long pageStart, long pageEnd) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        HashMap<String,Object> params = new HashMap<String,Object>();
        
        params.put("authorSite", site);
        params.put("authorCategory", category);
        params.put("pageStart", pageStart);
        params.put("pageEnd", pageEnd);
        
        List result = null;
        try {
            result = (List) session.selectList("xm.bibibiradio.mainsystem.webservice.dal.AuthorDAO.selectAuthorListOrderScore", params);
        } finally {
            session.close();
        }
           
       return result;
    }
    
    
}
