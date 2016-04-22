package xm.bibibiradio.mainsystem.webservice.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateProcessor implements JsonValueProcessor {
    private SimpleDateFormat sdf;
    private String formatDate = "yyyy-MM-dd HH:mm";
    
    public JsonDateProcessor(){
        sdf = new SimpleDateFormat(formatDate);
    }
    
    public JsonDateProcessor(String formatDate){
        this.formatDate = formatDate;
        sdf = new SimpleDateFormat(formatDate);
    }
    
    @Override
    public Object processArrayValue(Object arg0, JsonConfig arg1) {
        // TODO Auto-generated method stub
        return process(arg0);
    }

    @Override
    public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
        // TODO Auto-generated method stub
        return process(arg1);
    }
    
    private String process(Object date){
        if(date!=null && date instanceof Date){
            return sdf.format(date);
        }
        return "";
    }

}
