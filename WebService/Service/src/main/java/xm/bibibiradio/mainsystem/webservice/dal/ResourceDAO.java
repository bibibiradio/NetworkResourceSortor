package xm.bibibiradio.mainsystem.webservice.dal;

import java.util.List;
import java.util.Map;

import xm.bibibiradio.mainsystem.webservice.dal.dataobject.MostPvResource;

public interface ResourceDAO {
    public List<String> selectCategories();
    public List<MostPvResource> selectPvResource(Map<String, Object> dalMap);
}
