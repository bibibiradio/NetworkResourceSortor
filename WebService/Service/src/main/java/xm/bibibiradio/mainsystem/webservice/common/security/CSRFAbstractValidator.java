package xm.bibibiradio.mainsystem.webservice.common.security;

import java.util.Map;

import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioException;
import xm.bibibiradio.mainsystem.webservice.common.session.Session;

public abstract class CSRFAbstractValidator implements CSRFValidator {
    protected String sessionKeyName = "ctoken";
    protected String paramsKeyName = "ctoken";
    
    @Override
    public String generateCsrfToken(Session session) throws BibibiRadioException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean validatCsrfToken(Map<String, String> params, Session session)
                                                                                throws BibibiRadioException {
        // TODO Auto-generated method stub
        return false;
    }

    public String getSessionKeyName() {
        return sessionKeyName;
    }

    public void setSessionKeyName(String sessionKeyName) {
        this.sessionKeyName = sessionKeyName;
    }

    public String getParamsKeyName() {
        return paramsKeyName;
    }

    public void setParamsKeyName(String paramsKeyName) {
        this.paramsKeyName = paramsKeyName;
    }
    
    
}
