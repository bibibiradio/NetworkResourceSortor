package com.bibibiradio.executetesttime;

import java.util.Map;

public interface IExecuteTimeCallback {
    public void dealExecuteResults(Map<String,ExecuteResult> executeResults);
    public void init();
}
