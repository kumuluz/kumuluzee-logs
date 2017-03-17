/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created by Rok on 14. 03. 2017.
 *
 * @Author Rok Povse, Marko Skrjanec
 */
public class LogUtil {

    private static LogUtil logUtil;

    private Log logInstance;
    private LogCommons logCommonsInstance;
    private LogConfigurator logConfigurator;

    private Map<String,Log> loggers;

    private LogUtil() {
        loggers = new HashMap();
    }

    public static LogUtil getInstance() {
        if (logUtil == null) {
            logUtil = new LogUtil();
        }

        return logUtil;
    }

    public Log getLogInstance(String loggerName) {
        if (!loggers.containsKey(loggerName)) {
            loggers.put(loggerName, logInstance.getLogger(loggerName));
        }
        return loggers.get(loggerName);
    }

    public LogCommons getLogCommonsInstance() {
        return logCommonsInstance;
    }

    public LogConfigurator getLogConfigurator() {
        return logConfigurator;
    }
}
