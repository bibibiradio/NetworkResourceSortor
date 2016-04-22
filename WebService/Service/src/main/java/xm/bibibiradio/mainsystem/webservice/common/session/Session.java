package xm.bibibiradio.mainsystem.webservice.common.session;

import java.util.Map;

public class Session {
    private Map<Object,Object> session;
    private String sessionId;
    private boolean isDirty;
    private boolean needReGenSessId;
    private boolean isNull;
    private boolean needGenSessId;
    public Map<Object, Object> getSession() {
        return session;
    }
    public void setSession(Map<Object, Object> session) {
        this.session = session;
    }
    public boolean isDirty() {
        return isDirty;
    }
    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }
    public boolean isNeedReGenSessId() {
        return needReGenSessId;
    }
    public void setNeedReGenSessId(boolean needReGenSessId) {
        this.needReGenSessId = needReGenSessId;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public boolean isNull() {
        return isNull;
    }
    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }
    public boolean isNeedGenSessId() {
        return needGenSessId;
    }
    public void setNeedGenSessId(boolean needGenSessId) {
        this.needGenSessId = needGenSessId;
    }
    
}
