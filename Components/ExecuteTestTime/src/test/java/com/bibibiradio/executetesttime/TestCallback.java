package com.bibibiradio.executetesttime;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class TestCallback implements IExecuteTimeCallback {

    private int isInit = 0;
    private int numOfResults = -1;
    @Override
    public void dealExecuteResults(Map<String, ExecuteResult> executeResults) {
        // TODO Auto-generated method stub
        numOfResults = executeResults.size();
        String executeLogString = "";
        Set<Entry<String, ExecuteResult>> setExecuteTimes = executeResults.entrySet();
        Iterator<Entry<String, ExecuteResult>> iter = setExecuteTimes.iterator();
        while(iter.hasNext()){
            Entry<String, ExecuteResult> entry = iter.next();
            ExecuteResult executeResult = entry.getValue();
            executeLogString += entry.getKey()+" : "+executeResult.getSumTime()+"ms  "+executeResult.getMeanTime()+"ms  "+executeResult.getExecuteCount()+"\n";
        }
        System.out.println(executeLogString);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        isInit = 1;
    }
    
    

    public int getIsInit() {
        return isInit;
    }

    public void setIsInit(int isInit) {
        this.isInit = isInit;
    }

    public int getNumOfResults() {
        return numOfResults;
    }

    public void setNumOfResults(int numOfResults) {
        this.numOfResults = numOfResults;
    }
    
    

}
