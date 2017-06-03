package xm.bibibiradio.mainsystem.dal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ResourceDAOImpl implements ResourceDAO {
	private SqlSessionFactory sqlSessionFactory;

	@Override
	public Long selectMaxDataBySite(int rType,int rSite) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rType", rType);
		params.put("rSite", rSite);
		Long result = null;
		try {
			result = (Long) session
					.selectOne(
							"xm.bibibiradio.mainsystem.dal.ResourceDAO.selectMaxDataBySite",
							params);
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
			session.update(
					"xm.bibibiradio.mainsystem.dal.ResourceDAO.updateResource",
					resourceData);
			session.commit();
		} finally {
			session.close();
		}
	}

	@Override
	public long insertResource(ResourceData rData) {
		SqlSession session = sqlSessionFactory.openSession();

		try {
			return session.insert(
					"xm.bibibiradio.mainsystem.dal.ResourceDAO.insertResource",
					rData);
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
	public Long selectMinDataBySite(int rType,int rSite) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rType", rType);
		params.put("rSite", rSite);
		Long result = null;
		try {
			result = (Long) session
					.selectOne(
							"xm.bibibiradio.mainsystem.dal.ResourceDAO.selectMinDataBySite",
							params);
		} finally {
			session.close();
		}

		return result;
	}

	@Override
	public HashMap<String, Object> selectCommentByRid(long rId) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();

		HashMap<String, Object> result = null;
		try {
			result = (HashMap<String, Object>) session.selectOne(
					"xm.bibibiradio.mainsystem.dal.ResourceDAO.selectByRid",
					rId);
		} finally {
			session.close();
		}

		return result;
	}

	@Override
	public List<Long> selectDateSiteList(Date startDate, Date endDate, int rSite) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("rSite", rSite);
		List result = null;
		try {
			result = (List) session
					.selectList(
							"xm.bibibiradio.mainsystem.dal.ResourceDAO.selectDateSiteList",
							params);
		} finally {
			session.close();
		}

		return result;
	}

	@Override
	public List<Long> selectDateList(Date startDate, Date endDate, int rSite) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("rSite", rSite);
		List result = null;
		try {
			result = (List) session.selectList(
					"xm.bibibiradio.mainsystem.dal.ResourceDAO.selectDateList",
					params);
		} finally {
			session.close();
		}

		return result;
	}

	@Override
	public void deleteRid(long rId) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();

		try {
			session.delete(
					"xm.bibibiradio.mainsystem.dal.ResourceDAO.deleteRid", rId);
		} finally {
			session.close();
		}
	}

	@Override
	public Long selectByrInnerId(long rInnerId, int rSite) {
		// TODO Auto-generated method stub
		SqlSession session = sqlSessionFactory.openSession();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rInnerId", rInnerId);
		params.put("rSite", rSite);
		Long result = null;
		try {
			result = (Long) session
					.selectOne(
							"xm.bibibiradio.mainsystem.dal.ResourceDAO.selectByrInnerId",
							params);
		} finally {
			session.close();
		}

		return result;
	}

}
