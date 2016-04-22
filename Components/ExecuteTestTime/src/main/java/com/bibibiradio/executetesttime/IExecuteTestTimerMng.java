package com.bibibiradio.executetesttime;

import java.util.Properties;

public interface IExecuteTestTimerMng {
    final Properties propertyConfig = null;
    public void start(String name);
    public void end(String name);
}
