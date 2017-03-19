/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.messages.LogMessage;
import com.kumuluz.ee.logs.utils.Log4j2LogUtil;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;

/**
 * Kumuluz-logs logger interface
 *
 * @author Rok Povse, Marko Skrjanec
 */
public class Log4j2Logger implements Logger {

    private org.apache.logging.log4j.Logger logger;

    public Log4j2Logger() {
    }

    private Log4j2Logger(String logName) {
        logger = LogManager.getLogger(logName);
    }

    @Override
    public Logger getLogger(String logName) {
        Log4j2Logger log4j2Log = new Log4j2Logger(logName);
        return log4j2Log;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void log(LogLevel level, String message) {
        logger.log(Log4j2LogUtil.convertToLog4j2Level(level), message);
    }

    @Override
    public void log(LogLevel level, String message, Object... args) {
        logger.log(Log4j2LogUtil.convertToLog4j2Level(level), message, args);
    }

    @Override
    public void log(LogLevel level, String message, Throwable thrown) {
        logger.log(Log4j2LogUtil.convertToLog4j2Level(level), message, thrown);
    }

    @Override
    public void log(LogLevel level, Throwable thrown) {
        logger.log(Log4j2LogUtil.convertToLog4j2Level(level), thrown);
    }

    @Override
    public void log(LogLevel level, LogMessage message) {
        log(level, message, null);
    }

    @Override
    public void log(LogLevel level, LogMessage message, Throwable thrown) {
        if (message == null) {
            logger.log(Log4j2LogUtil.convertToLog4j2Level(level), message, thrown);
        } else if (message.getFields() == null) {
            logger.log(Log4j2LogUtil.convertToLog4j2Level(level), message.getMessage(), thrown);
        } else {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(message.getFields())) {
                logger.log(Log4j2LogUtil.convertToLog4j2Level(level), message.getMessage(), thrown);
            }
        }
    }

    @Override
    public void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    @Override
    public void trace(String message, Object... args) {
        log(LogLevel.TRACE, message, args);
    }

    @Override
    public void trace(String message, Throwable thrown) {
        log(LogLevel.TRACE, message, thrown);
    }

    @Override
    public void trace(Throwable thrown) {
        log(LogLevel.TRACE, thrown);
    }

    @Override
    public void trace(LogMessage message) {
        log(LogLevel.TRACE, message);
    }

    @Override
    public void trace(LogMessage message, Throwable thrown) {
        log(LogLevel.TRACE, message, thrown);
    }

    @Override
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    @Override
    public void info(String message, Object... args) {
        log(LogLevel.INFO, message, args);
    }

    @Override
    public void info(String message, Throwable thrown) {
        log(LogLevel.INFO, message, thrown);
    }

    @Override
    public void info(Throwable thrown) {
        log(LogLevel.INFO, thrown);
    }

    @Override
    public void info(LogMessage message) {
        log(LogLevel.INFO, message);
    }

    @Override
    public void info(LogMessage message, Throwable thrown) {
        log(LogLevel.INFO, message, thrown);
    }

    @Override
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    @Override
    public void debug(String message, Object... args) {
        log(LogLevel.DEBUG, message, args);
    }

    @Override
    public void debug(String message, Throwable thrown) {
        log(LogLevel.DEBUG, message, thrown);
    }

    @Override
    public void debug(Throwable thrown) {
        log(LogLevel.DEBUG, thrown);
    }

    @Override
    public void debug(LogMessage message) {
        log(LogLevel.DEBUG, message);
    }

    @Override
    public void debug(LogMessage message, Throwable thrown) {
        log(LogLevel.DEBUG, message, thrown);
    }

    @Override
    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    @Override
    public void warn(String message, Object... args) {
        log(LogLevel.WARN, message, args);
    }

    @Override
    public void warn(String message, Throwable thrown) {
        log(LogLevel.WARN, message, thrown);
    }

    @Override
    public void warn(Throwable thrown) {
        log(LogLevel.WARN, thrown);
    }

    @Override
    public void warn(LogMessage message) {
        log(LogLevel.WARN, message);
    }

    @Override
    public void warn(LogMessage message, Throwable thrown) {
        log(LogLevel.WARN, message, thrown);
    }

    @Override
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    @Override
    public void error(String message, Object... args) {
        log(LogLevel.ERROR, message, args);
    }

    @Override
    public void error(String message, Throwable thrown) {
        log(LogLevel.ERROR, message, thrown);
    }

    @Override
    public void error(Throwable thrown) {
        log(LogLevel.ERROR, thrown);
    }

    @Override
    public void error(LogMessage message) {
        log(LogLevel.ERROR, message);
    }

    @Override
    public void error(LogMessage message, Throwable thrown) {
        log(LogLevel.ERROR, message, thrown);
    }

}
