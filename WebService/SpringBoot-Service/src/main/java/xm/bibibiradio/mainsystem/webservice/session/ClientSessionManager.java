package xm.bibibiradio.mainsystem.webservice.session;

import javax.servlet.http.HttpSession;

public class ClientSessionManager implements SessionManager {

    @Override
    public HttpSession getSession(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSession(String id, HttpSession sess) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSession(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public String genSessionId() {
        // TODO Auto-generated method stub
        return "321";
    }

    @Override
    public String reGenSessionId(HttpSession sess) {
        // TODO Auto-generated method stub
        return "123";
    }

    @Override
    public long getAllSessionNum() {
        // TODO Auto-generated method stub
        return 0;
    }

}
