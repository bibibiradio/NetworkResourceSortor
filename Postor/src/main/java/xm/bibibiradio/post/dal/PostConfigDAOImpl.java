package xm.bibibiradio.post.dal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import xm.bibibiradio.post.biz.PostConfigData;
import xm.bibibiradio.post.biz.PostResourceData;

public class PostConfigDAOImpl implements PostConfigDAO {
    private SqlSessionFactory sqlSessionFactory;
    
    @Override
    public List<PostConfigData> selectAllPostConfigData() {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        List result = null;
        try {
            result = (List) session.selectList(
                "xm.bibibiradio.post.dal.PostConfigDAO.selectAllPostConfigData");
        } finally {
            session.close();
        }

        return result;
    }

    @Override
    public List<PostResourceData> selectNeedPostResourceData(PostConfigData postConfig) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("postType", postConfig.getPostType());
        params.put("postSite", postConfig.getPostSite());
        params.put("postCategory", postConfig.getPostCategory());
        params.put("postNum", postConfig.getPostNum());
        params.put("postTag", postConfig.getPostTags());
        params.put("rGmtCreateStart", new Date(postConfig.getLastPost().getTime()));
        params.put("rGmtCreateEnd", new Date());
        params.put("postLimitScore", postConfig.getPostLimitScore());
        
        List result = null;
        try {
            result = (List) session.selectList("xm.bibibiradio.post.dal.PostConfigDAO.selectNeedPostResourceData", params);
        } finally {
            session.close();
        }
           
       return result;
    }
    
    @Override
    public void updatePostConfigLastPost(Date lastPost, long postConfigId) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();
        
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("lastPost", lastPost);
        params.put("postConfId", postConfigId);
        
        try {
            session.update("xm.bibibiradio.post.dal.PostConfigDAO.updatePostConfigLastPost", params);
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
