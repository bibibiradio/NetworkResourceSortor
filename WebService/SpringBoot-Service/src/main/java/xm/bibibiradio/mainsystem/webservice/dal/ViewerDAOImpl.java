package xm.bibibiradio.mainsystem.webservice.dal;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import xm.bibibiradio.mainsystem.webservice.biz.ViewerScoreData;

public class ViewerDAOImpl implements ViewerDAO {
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public List<ViewerScoreData> selectViewerListOrderScore(int site, String category,
                                                            long pageStart, long pageEnd) {
        // TODO Auto-generated method stub
        SqlSession session = sqlSessionFactory.openSession();

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("viewerSite", site);
        params.put("viewerCategory", category);
        params.put("pageStart", pageStart);
        params.put("pageEnd", pageEnd);

        List result = null;
        try {
            result = (List) session.selectList(
                "xm.bibibiradio.mainsystem.webservice.dal.ViewerDAO.selectViewerListOrderScore",
                params);
        } finally {
            session.close();
        }

        return result;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

}
