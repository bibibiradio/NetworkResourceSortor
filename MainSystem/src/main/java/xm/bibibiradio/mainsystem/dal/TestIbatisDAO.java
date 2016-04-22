package xm.bibibiradio.mainsystem.dal;

import java.util.List;

public interface TestIbatisDAO {
	public TestIbatisData queryObject(long intKey);
	public List<TestIbatisData> queryList(long minIntId,long maxIntId);
	public List<TestIbatisData> queryListByIntKey(long minIntKey,long maxIntKey);
	public long insertObject(TestIbatisData testIbatisData);
	public void updateObject(TestIbatisData testIbatisData);
	public void deleteAll();
}
