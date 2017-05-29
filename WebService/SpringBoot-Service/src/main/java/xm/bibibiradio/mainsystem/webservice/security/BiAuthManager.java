package xm.bibibiradio.mainsystem.webservice.security;

import java.util.List;

public class BiAuthManager implements AuthManager{
    
    private User2RoleManager u2rMng;
    private Role2AuthUrlFeatureManager r2uMng;
    
    public boolean isAccess(String uid,String uri) throws Exception{
        List<Integer> roleCodes = u2rMng.user2RoleCode(uid);
        if(roleCodes == null)
            return false;
        
        for(Integer role:roleCodes){
            List<String> urlFeatures = r2uMng.role2AuthUrlFeature(role);
            if(urlFeatures == null)
                continue;
            
            for(String urlFeature:urlFeatures){
                if(uri.startsWith(urlFeature))
                    return true;
            }
        }
        
        return false;
    }

    public User2RoleManager getU2rMng() {
        return u2rMng;
    }

    public void setU2rMng(User2RoleManager u2rMng) {
        this.u2rMng = u2rMng;
    }

    public Role2AuthUrlFeatureManager getR2uMng() {
        return r2uMng;
    }

    public void setR2uMng(Role2AuthUrlFeatureManager r2uMng) {
        this.r2uMng = r2uMng;
    }
    
    
}
