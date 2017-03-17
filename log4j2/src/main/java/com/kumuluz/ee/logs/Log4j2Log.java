/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.messages.LogMessage;

/**
 * Kumuluz-logs logger interface
 *
 * @author Rok Povse, Marko Skrjanec
 */
public class Log4j2Log implements Log {

    @Override
    public Log getLogger(String logName) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void log(LogLevel level, String message) {

    }

    @Override
    public void log(LogLevel level, String message, Object... args) {

    }

    @Override
    public void log(LogLevel level, String message, Throwable thrown) {

    }

    @Override
    public void log(LogLevel level, Throwable thrown) {

    }

    @Override
    public void log(LogLevel level, LogMessage message) {

    }

    @Override
    public void log(LogLevel level, LogMessage message, Throwable thrown) {

    }

    @Override
    public void trace(String message) {

    }

    @Override
    public void trace(String message, Object... args) {

    }

    @Override
    public void trace(String message, Throwable thrown) {

    }

    @Override
    public void trace(Throwable thrown) {

    }

    @Override
    public void trace(LogMessage message) {

    }

    @Override
    public void trace(LogMessage message, Throwable thrown) {

    }

    @Override
    public void info(String message) {

    }

    @Override
    public void info(String message, Object... args) {

    }

    @Override
    public void info(String message, Throwable thrown) {

    }

    @Override
    public void info(Throwable thrown) {

    }

    @Override
    public void info(LogMessage message) {

    }

    @Override
    public void info(LogMessage message, Throwable thrown) {

    }

    @Override
    public void debug(String message) {

    }

    @Override
    public void debug(String message, Object... args) {

    }

    @Override
    public void debug(String message, Throwable thrown) {

    }

    @Override
    public void debug(Throwable thrown) {

    }

    @Override
    public void debug(LogMessage message) {

    }

    @Override
    public void debug(LogMessage message, Throwable thrown) {

    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void warn(String message, Object... args) {

    }

    @Override
    public void warn(String message, Throwable thrown) {

    }

    @Override
    public void warn(Throwable thrown) {

    }

    @Override
    public void warn(LogMessage message) {

    }

    @Override
    public void warn(LogMessage message, Throwable thrown) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public void error(String message, Object... args) {

    }

    @Override
    public void error(String message, Throwable thrown) {

    }

    @Override
    public void error(Throwable thrown) {

    }

    @Override
    public void error(LogMessage message) {

    }

    @Override
    public void error(LogMessage message, Throwable thrown) {

    }
}
