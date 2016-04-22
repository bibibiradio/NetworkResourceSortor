package xm.bibibiradio.mainsystem.webservice.common.session;

import java.util.Map;

public interface SessionManager {
    public Map<Object,Object> getSession(String key);
    public void setSession(String key,Map<Object,Object> value);
    public void removeSession(String key);
    public String genSessionKey();
}
