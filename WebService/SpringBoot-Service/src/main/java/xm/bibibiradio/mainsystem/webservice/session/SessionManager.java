package xm.bibibiradio.mainsystem.webservice.session;

import javax.servlet.http.HttpSession;

public interface SessionManager {
    public static String SESSION_ATTR_KEY = "__SESSION_KEY";
    public static long SESSION_INVALID_TIME = 1000000;
    public static String SESSIONID_REG_KEY = "__SESSION_REG";
    public static String SESSION_MNG_KEY = "__SESSION_MNG";
    
    public HttpSession getSession(String id);
    public void setSession(String id,HttpSession sess);
    public void removeSession(String id);
    public String genSessionId();
    public String reGenSessionId(HttpSession sess);
    public long getAllSessionNum();
}
