package com.bibibiradio.executetesttime;

public class TestExecuteThread extends Thread {
    @Override
    public void run(){
        for(int i=0;i<7;i++){
            ExecuteTimeTimerThreadImpl.GetExecuteTimeTimerMng().start("A");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ExecuteTimeTimerThreadImpl.GetExecuteTimeTimerMng().end("A");
        } 
        
        for(int i=0;i<7;i++){
            ExecuteTimeTimerThreadImpl.GetExecuteTimeTimerMng().start("B");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ExecuteTimeTimerThreadImpl.GetExecuteTimeTimerMng().end("B");
        }
        
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        ExecuteTimeTimerThreadImpl.GetExecuteTimeTimerMng().start("C");
        ExecuteTimeTimerThreadImpl.GetExecuteTimeTimerMng().end("C");
    }
}
