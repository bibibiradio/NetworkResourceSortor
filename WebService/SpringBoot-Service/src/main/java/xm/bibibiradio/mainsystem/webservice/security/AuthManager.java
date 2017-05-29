package xm.bibibiradio.mainsystem.webservice.security;

public interface AuthManager{
    public boolean isAccess(String uid,String uri) throws Exception;
}
