/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs.utils;

import com.kumuluz.ee.logs.enums.LogLevel;
import org.apache.logging.log4j.Level;

/**
 * Kumuluz-logs logger interface
 *
 * @author Rok Povse, Marko Skrjanec
 */
public class Log4j2LogUtil {

    public static final String LOG4J2_LOGGER_NAME = "Log4j2Logger";

    public static Level convertToLog4j2Level(LogLevel logLevel) {
        return Level.getLevel(logLevel.toString());
    }

    public static LogLevel convertFromLog4j2Level(Level level) {
        return LogLevel.valueOf(level.name());
    }
}
