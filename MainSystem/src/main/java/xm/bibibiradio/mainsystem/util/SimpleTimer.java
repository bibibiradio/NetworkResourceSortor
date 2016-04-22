package xm.bibibiradio.mainsystem.util;

public class SimpleTimer {
    private long alertTime;
    
    public long isAlert(){
        return alertTime - System.currentTimeMillis();
    }

    public long getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(long alertTime) {
        this.alertTime = alertTime;
    }
    
    
}
