package xm.bibibiradio.mainsystem.webservice.security;

import java.util.List;

public interface User2RoleManager {
    public List<Integer> user2RoleCode(String uid) throws Exception;
}
