package com.kumuluz.ee.logs;

import com.kumuluz.ee.logs.enums.LogLevel;

/**
 * Created by Rok on 17. 03. 2017.
 *
 * @Author Rok Povše
 */
public class Log4j2LogConfigurator implements LogConfigurator {

    @Override
    public void init() {

    }

    @Override
    public void setLevel(String logName, LogLevel logLevel) {

    }

    @Override
    public LogLevel getLevel(String logName) {
        return null;
    }
}
