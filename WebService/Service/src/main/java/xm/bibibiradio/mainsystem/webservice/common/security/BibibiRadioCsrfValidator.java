package xm.bibibiradio.mainsystem.webservice.common.security;

import java.util.Map;
import java.util.Random;

import xm.bibibiradio.mainsystem.webservice.common.controller.BibibiRadioException;
import xm.bibibiradio.mainsystem.webservice.common.session.Session;

public class BibibiRadioCsrfValidator extends CSRFAbstractValidator implements CSRFValidator {
    static final private Random rand = new Random();
    @Override
    public String generateCsrfToken(Session session) throws BibibiRadioException {
        // TODO Auto-generated method stub
        rand.setSeed(System.currentTimeMillis());
        session.getSession().put(sessionKeyName, "123");
        return "123";
    }

    @Override
    public boolean validatCsrfToken(Map<String, String> params, Session session)
                                                                                throws BibibiRadioException {
        // TODO Auto-generated method stub
        return false;
    }
}
