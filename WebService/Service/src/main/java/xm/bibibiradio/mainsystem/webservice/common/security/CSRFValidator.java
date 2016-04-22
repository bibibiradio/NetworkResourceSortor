package xm.bibibiradio.mainsystem.webservice.common.security;

import java.util.Map;

import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioException;
import xm.bibibiradio.mainsystem.webservice.common.session.Session;

public interface CSRFValidator {
    public String generateCsrfToken(Session session) throws BibibiRadioException;
    public boolean validatCsrfToken(Map<String,String> params,Session session) throws BibibiRadioException;
}
