/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.markers.CommonsMarker;
import com.kumuluz.ee.logs.markers.Marker;
import com.kumuluz.ee.logs.messages.LogMessage;
import com.kumuluz.ee.logs.types.*;
import com.kumuluz.ee.logs.utils.Log4j2LogUtil;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

/**
 * Kumuluz-logs logger interface
 *
 * @author Rok Povse, Marko Skrjanec
 */
public class Log4j2LogCommons implements LogCommons {


    private static final String METRIC_RESPONSE_TIME = "response_time";

    private static LogLevel DEFAULT_LOG_LEVEL = LogLevel.TRACE;

    private org.apache.logging.log4j.Logger logger;


    private Log4j2LogCommons(String logName) {
        logger = LogManager.getLogger(logName);
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public Log4j2LogCommons getCommonsLogger(String logName) {
        Log4j2LogCommons log4j2LogCommonsLogger = new Log4j2LogCommons(logName);
        return log4j2LogCommonsLogger;
    }

    @Override
    public void setDefaultLevel(LogLevel logLevel) {
        this.DEFAULT_LOG_LEVEL = logLevel;
    }

    @Override
    public LogMethodContext logMethodEntry(LogMethodMessage logMethodMessage) {
        return null;
    }

    @Override
    public LogMethodContext logMethodEntry(LogLevel level, LogMethodMessage logMethodMessage) {
        return null;
    }

    @Override
    public void logMethodExit(LogMethodContext logMethodContext) {

    }

    @Override
    public LogResourceContext logResourceStart(LogResourceMessage logResourceMessage) {
        return null;
    }

    @Override
    public LogResourceContext logResourceStart(Marker marker, LogResourceMessage logResourceMessage) {
        return null;
    }

    @Override
    public LogResourceContext logResourceStart(LogLevel level, Marker marker, LogResourceMessage logResourceMessage) {
        return null;
    }

    @Override
    public void logResourceEnd(LogResourceContext logResourceContext) {

    }

//    @Override
//    public void logMethodEntry(LogMessage logMessage) {
//        logMethodEntry(DEFAULT_LOG_LEVEL, logMessage);
//    }
//
//    @Override
//    public void logMethodEntry(LogLevel level, LogMessage logMessage) {
//        log(level, CommonsMarker.ENTRY, logMessage);
//    }
//
//    @Override
//    public void logMethodExit(LogMessage logMessage) {
//        logMethodExit(DEFAULT_LOG_LEVEL, logMessage);
//    }
//
//    @Override
//    public void logMethodExit(LogLevel level, LogMessage logMessage) {
//        log(level, CommonsMarker.EXIT, logMessage);
//    }
//
//    @Override
//    public LogMetrics logMethodEntryMetrics(LogMessage logMessage) {
//        return logMethodEntryMetrics(DEFAULT_LOG_LEVEL, logMessage);
//    }
//
//    @Override
//    public LogMetrics logMethodEntryMetrics(LogLevel level, LogMessage logMessage) {
//        log(level, CommonsMarker.ENTRY, logMessage);
//        return new LogMetrics();
//    }
//
//    @Override
//    public void logMethodExitMetrics(LogMessage logMessage, LogMetrics logMetrics) {
//        logMethodExitMetrics(DEFAULT_LOG_LEVEL, logMessage, logMetrics);
//    }
//
//    @Override
//    public void logMethodExitMetrics(LogLevel level, LogMessage logMessage, LogMetrics logMetrics) {
//        logMessage.getFields().put(METRIC_RESPONSE_TIME, logMetrics.getTimeElapsed().toString());
//        log(level, CommonsMarker.EXIT, logMessage);
//    }

//    @Override
//    public void logInvokeResourceStart(Marker marker, LogMessage logMessage) {
//        logInvokeResourceStart(DEFAULT_LOG_LEVEL, marker, logMessage);
//    }
//
//    @Override
//    public void logInvokeResourceStart(LogLevel level, Marker marker, LogMessage logMessage) {
//        log(level, marker, logMessage);
//    }
//
//    @Override
//    public void logInvokeResourceEnd(Marker marker, LogMessage logMessage) {
//        logInvokeResourceEnd(DEFAULT_LOG_LEVEL, marker, logMessage);
//    }
//
//    @Override
//    public void logInvokeResourceEnd(LogLevel level, Marker marker, LogMessage logMessage) {
//        log(level, marker, logMessage);
//    }
//
//    @Override
//    public LogMetrics logInvokeResourceStartMetrics(Marker marker, LogMessage logMessage) {
//        return logInvokeResourceStartMetrics(DEFAULT_LOG_LEVEL, marker, logMessage);
//    }
//
//    @Override
//    public LogMetrics logInvokeResourceStartMetrics(LogLevel level, Marker marker, LogMessage logMessage) {
//        log(level, marker, logMessage);
//        return new LogMetrics();
//    }
//
//    @Override
//    public void logInvokeResourceEndMetrics(Marker marker, LogMessage logMessage, LogMetrics logMetrics) {
//        logInvokeResourceEndMetrics(DEFAULT_LOG_LEVEL, marker, logMessage, logMetrics);
//    }
//
//    @Override
//    public void logInvokeResourceEndMetrics(LogLevel level, Marker marker, LogMessage logMessage, LogMetrics
//            logMetrics) {
//        logMessage.getFields().put(METRIC_RESPONSE_TIME, logMetrics.getTimeElapsed().toString());
//        log(level, marker, logMessage);
//    }

    /**
     * @param level      object defining Level
     * @param marker     object defining Marker
     * @param logMessage object defining LogMessage
     */
    private void log(LogLevel level, Marker marker, LogMessage logMessage) {
        if (logMessage.getFields() != null) {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(logMessage.getFields())) {
                logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString()),
                        logMessage.getMessage());
            }
        } else {
            logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString()), logMessage
                    .getMessage());
        }
    }
}
