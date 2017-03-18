package com.kumuluz.ee.logs;


import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.utils.Log4j2LogUtil;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;


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
        Configurator.setLevel(logName, Log4j2LogUtil.convertToLog4j2Level(logLevel));
    }

    @Override
    public LogLevel getLevel(String logName) {
        return Log4j2LogUtil.convertFromLog4j2Level(LogManager.getLogger(logName).getLevel());
    }
}
