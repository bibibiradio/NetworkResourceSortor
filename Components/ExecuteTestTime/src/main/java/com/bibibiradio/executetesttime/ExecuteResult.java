package com.bibibiradio.executetesttime;

public class ExecuteResult {
    private long sumTime;
    private long meanTime;
    private long executeCount;
    public long getSumTime() {
        return sumTime;
    }
    public void setSumTime(long sumTime) {
        this.sumTime = sumTime;
    }
    public long getMeanTime() {
        return meanTime;
    }
    public void setMeanTime(long meanTime) {
        this.meanTime = meanTime;
    }
    public long getExecuteCount() {
        return executeCount;
    }
    public void setExecuteCount(long executeCount) {
        this.executeCount = executeCount;
    }
}
