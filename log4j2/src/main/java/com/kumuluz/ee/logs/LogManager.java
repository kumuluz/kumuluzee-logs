/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import com.kumuluz.ee.logs.enums.LogLevel;

/**
 * Created by Rok on 14. 03. 2017.
 *
 * @Author Rok Povse, Marko Skrjanec
 */
public class LogManager {

    public static Log getLogger(String loggerName) {
        return  LogUtil.getInstance().getLogInstance(loggerName);
    }

    public static LogLevel getLogLevel(String loggerName) {
        return LogUtil.getInstance().getLogConfigurator().getLevel(loggerName);
    }

    public static void setLogLevel(String loggerName, LogLevel logLevel) {
        LogUtil.getInstance().getLogConfigurator().setLevel(loggerName,logLevel);
    }
}
