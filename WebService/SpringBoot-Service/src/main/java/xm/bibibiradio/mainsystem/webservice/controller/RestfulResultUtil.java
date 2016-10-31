package xm.bibibiradio.mainsystem.webservice.controller;

public class RestfulResultUtil {
    static public RestfulResult newErrorRestfulResult(Exception ex){
        RestfulResult r = new RestfulResult();
        r.setErrorMessage(ex.getMessage());
        return r;
    }
    
    static public RestfulResult newRestfulResult(Object data){
        RestfulResult r = new RestfulResult();
        r.setSuccess(true);
        r.setErrorMessage("");
        r.setData(data);
        return r;
    }
}
