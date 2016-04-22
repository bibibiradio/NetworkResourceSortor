package xm.bibibiradio.mainsystem.webservice.common.controller;

import java.util.Map;

public class ViewResult {
    private String view;
    private Map<String,Object> result;
    public String getView() {
        return view;
    }
    public void setView(String view) {
        this.view = view;
    }
    public Map<String, Object> getResult() {
        return result;
    }
    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
    
    
}
