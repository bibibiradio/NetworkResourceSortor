package com.bibibiradio.executetesttime;

public class ExecuteTimeData {
    private long sumStartTime;
    private long sumEndTime;
    private long executeCount;
    
    public long getSumStartTime() {
        return sumStartTime;
    }
    public void setSumStartTime(long sumStartTime) {
        this.sumStartTime = sumStartTime;
    }
    public long getSumEndTime() {
        return sumEndTime;
    }
    public void setSumEndTime(long sumEndTime) {
        this.sumEndTime = sumEndTime;
    }
    public long getExecuteCount() {
        return executeCount;
    }
    public void setExecuteCount(long executeCount) {
        this.executeCount = executeCount;
    }
    public void addExecuteCount(){
        executeCount+=1;
    }
}
