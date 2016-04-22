package com.bibibiradio.executetesttime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class ExecuteTimeTimerMngImpl implements IExecuteTestTimerMng {
    private static IExecuteTestTimerMng ExecuteTimeTimerMngImpl = null;
    private static Properties propertyConfig = null;
    
    private Map<String,ExecuteTimeData> executeTimes;
    private long startTime;
    private int balanceCount;
    
    private long rollTime = -1;
    private OutputStream outputStream = null;
    private IExecuteTimeCallback userCallback = null;
    
    static public IExecuteTestTimerMng GetExecuteTimeTimerMng(){
        if(ExecuteTimeTimerMngImpl == null){
            ExecuteTimeTimerMngImpl = new ExecuteTimeTimerMngImpl(propertyConfig);
        }
        return ExecuteTimeTimerMngImpl;
    }
    
    private ExecuteTimeTimerMngImpl(Properties propertyConfig){
        executeTimes = new HashMap<String,ExecuteTimeData>();
        startTime = System.currentTimeMillis();
        balanceCount = 0;
        double fileRandom = 0;
        
        SecureRandom random = new SecureRandom();
        fileRandom = random.nextDouble();
        
        String outputFilePath = propertyConfig.getProperty("outputFilePath");
        String consoleLog = propertyConfig.getProperty("consoleLog");
        String strRollTime = propertyConfig.getProperty("rollTime");
        String callBack = propertyConfig.getProperty("callBack");
        try{
            if(consoleLog == null && outputFilePath == null){
                outputStream = new FileOutputStream("ExecuteTimeLog.txt_"+fileRandom,true);
            }
            
            if(outputFilePath != null){
                outputStream = new FileOutputStream(outputFilePath+"_"+fileRandom,true);
            }else if(consoleLog.equals("stdout")){
                outputStream = System.out;
            }else if(consoleLog.equals("stderr")){
                outputStream = System.err;
            }
        }catch(Exception e){
            //init outputStream Exception
        }
        
        if(callBack != null){
            try {
                userCallback = (IExecuteTimeCallback) getClass().getClassLoader().loadClass(callBack).newInstance();
                userCallback.init();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        if(strRollTime != null){
            rollTime = Long.valueOf(strRollTime);
        }else{
            rollTime = 20000;
        }
    }
    
    @Override
    public void start(String name) {
        // TODO Auto-generated method stub
        ExecuteTimeData executeTimeData = executeTimes.get(name);
        if(executeTimeData != null){
            executeTimeData.setSumStartTime(executeTimeData.getSumStartTime()+System.currentTimeMillis());
        }else{
            executeTimeData = new ExecuteTimeData();
            executeTimeData.setSumStartTime(System.currentTimeMillis());
            executeTimeData.setSumEndTime(0);
            executeTimeData.setExecuteCount(0);
            executeTimes.put(name, executeTimeData);
        }
        balanceCount += 1;
    }

    @Override
    public void end(String name) {
        // TODO Auto-generated method stub
        ExecuteTimeData executeTimeData = executeTimes.get(name);
        if(executeTimeData == null){
            return;
        }
        executeTimeData.setSumEndTime(executeTimeData.getSumEndTime()+System.currentTimeMillis());
        executeTimeData.addExecuteCount();
        
        balanceCount -= 1;
        
        checkNeedOutput();
    }
    
    private void reset(){
        startTime = System.currentTimeMillis();
        balanceCount = 0;
        executeTimes.clear();
    }
    
    private Map<String,ExecuteResult> getExecuteResults(){
        Map<String,ExecuteResult> executeResults = new HashMap<String,ExecuteResult>();
        Set<Entry<String, ExecuteTimeData>> setExecuteTimes = executeTimes.entrySet();
        Iterator<Entry<String, ExecuteTimeData>> iter = setExecuteTimes.iterator();
        while(iter.hasNext()){
            Entry<String, ExecuteTimeData> entry = iter.next();
            ExecuteTimeData executeTimeData = entry.getValue();
            
            ExecuteResult executeResult = new ExecuteResult();
            executeResult.setExecuteCount(executeTimeData.getExecuteCount());
            executeResult.setMeanTime((executeTimeData.getSumEndTime()-executeTimeData.getSumStartTime())/executeTimeData.getExecuteCount());
            executeResult.setSumTime(executeTimeData.getSumEndTime()-executeTimeData.getSumStartTime());
            executeResults.put(entry.getKey(), executeResult);
            
            executeTimeData.setExecuteCount(1);
            executeTimeData.setSumStartTime(0);
            executeTimeData.setSumEndTime(0);
        }
        return executeResults;
    }
    
    private void outputLogString(Map<String,ExecuteResult> executeResults){
        String executeLogString = "--------------"+(new Date().toString())+"-------------\n";
        Set<Entry<String, ExecuteResult>> setExecuteTimes = executeResults.entrySet();
        Iterator<Entry<String, ExecuteResult>> iter = setExecuteTimes.iterator();
        while(iter.hasNext()){
            Entry<String, ExecuteResult> entry = iter.next();
            ExecuteResult executeResult = entry.getValue();
            executeLogString += entry.getKey()+" : "+executeResult.getSumTime()+"ms  "+executeResult.getMeanTime()+"ms  "+executeResult.getExecuteCount()+"\n";
        }
        executeLogString += "\n\n";
        if(outputStream != null){
            try {
                outputStream.write(executeLogString.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    private void checkNeedOutput(){
        if(((rollTime != -1 && (System.currentTimeMillis() - startTime) >= rollTime)) && balanceCount <= 0){
            Map<String,ExecuteResult> executeResults = getExecuteResults();
            if(userCallback != null){
                userCallback.dealExecuteResults(executeResults);
            }else{
                outputLogString(executeResults);
            }
            
            balanceCount = 0;
            startTime = System.currentTimeMillis();
            
        }else if(rollTime != -1 && (System.currentTimeMillis() - startTime) >= 4*rollTime){
            reset();
        }
    }
    
    

    public static Properties getPropertyConfig() {
        return propertyConfig;
    }

    public static void setPropertyConfig(Properties inputPropertyConfig) {
        propertyConfig = inputPropertyConfig;
    }

    public long getRollTime() {
        return rollTime;
    }

    public void setRollTime(long rollTime) {
        this.rollTime = rollTime;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Map<String, ExecuteTimeData> getExecuteTimes() {
        return executeTimes;
    }

    public void setExecuteTimes(Map<String, ExecuteTimeData> executeTimes) {
        this.executeTimes = executeTimes;
    }

    public IExecuteTimeCallback getUserCallback() {
        return userCallback;
    }

    public void setUserCallback(IExecuteTimeCallback userCallback) {
        this.userCallback = userCallback;
    }
}
