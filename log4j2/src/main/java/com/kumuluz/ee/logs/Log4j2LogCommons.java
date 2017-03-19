/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.markers.CommonsMarker;
import com.kumuluz.ee.logs.markers.Marker;
import com.kumuluz.ee.logs.messages.LogMessage;
import com.kumuluz.ee.logs.types.LogMethodContext;
import com.kumuluz.ee.logs.types.LogMethodMessage;
import com.kumuluz.ee.logs.types.LogResourceContext;
import com.kumuluz.ee.logs.types.LogResourceMessage;
import com.kumuluz.ee.logs.utils.Log4j2LogUtil;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;

/**
 * Kumuluz-logs logger interface
 *
 * @author Rok Povse, Marko Skrjanec
 */
public class Log4j2LogCommons implements LogCommons {


    private static final String METRIC_RESPONSE_TIME = "response-time";

    private static LogLevel DEFAULT_LOG_LEVEL = LogLevel.TRACE;

    private org.apache.logging.log4j.Logger logger;

    public Log4j2LogCommons() {

    }

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
        log(DEFAULT_LOG_LEVEL, CommonsMarker.ENTRY, logMethodMessage.getCallMessage());
        return new LogMethodContext(logMethodMessage, DEFAULT_LOG_LEVEL);
    }

    @Override
    public LogMethodContext logMethodEntry(LogLevel level, LogMethodMessage logMethodMessage) {
        log(level, CommonsMarker.ENTRY, logMethodMessage.getCallMessage());
        return new LogMethodContext(logMethodMessage, level);
    }

    @Override
    public void logMethodExit(LogMethodContext logMethodContext) {
        if (logMethodContext.isMetricsEnabled() != null && logMethodContext.isMetricsEnabled()) {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put(METRIC_RESPONSE_TIME,
                    logMethodContext.getLogMetrics().getTimeElapsed().toString())) {
                log(logMethodContext.getLevel(), CommonsMarker.EXIT, logMethodContext.getCallExitMessage());
            }
        } else {
            log(logMethodContext.getLevel(), CommonsMarker.EXIT, logMethodContext.getCallExitMessage());
        }
    }

    @Override
    public LogResourceContext logResourceStart(LogResourceMessage logResourceMessage) {
        log(DEFAULT_LOG_LEVEL, CommonsMarker.RESOURCE_START, logResourceMessage.getInvokeMessage());
        return new LogResourceContext(logResourceMessage, DEFAULT_LOG_LEVEL, CommonsMarker.RESOURCE_START);
    }

    @Override
    public LogResourceContext logResourceStart(Marker marker, LogResourceMessage logResourceMessage) {
        log(DEFAULT_LOG_LEVEL, marker, logResourceMessage.getInvokeMessage());
        return new LogResourceContext(logResourceMessage, DEFAULT_LOG_LEVEL, marker);
    }

    @Override
    public LogResourceContext logResourceStart(LogLevel level, Marker marker, LogResourceMessage logResourceMessage) {
        log(level, marker, logResourceMessage.getInvokeMessage());
        return new LogResourceContext(logResourceMessage, level, marker);
    }

    @Override
    public void logResourceEnd(LogResourceContext logResourceContext) {
        if (logResourceContext.isMetricsEnabled() != null && logResourceContext.isMetricsEnabled()) {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put(METRIC_RESPONSE_TIME,
                    logResourceContext.getLogMetrics().getTimeElapsed().toString())) {
                log(logResourceContext.getLevel(), logResourceContext.getMarker(), logResourceContext
                        .getInvokeEndMessage());
            }
        } else {
            log(logResourceContext.getLevel(), logResourceContext.getMarker(), logResourceContext.getInvokeEndMessage
                    ());
        }
    }

    /**
     * @param level      object defining Level
     * @param marker     object defining Marker
     * @param logMessage object defining LogMessage
     */
    private void log(LogLevel level, Marker marker, LogMessage logMessage) {
        if (logMessage == null) {
            logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString()));
        } else if (logMessage.getFields() == null) {
            logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString()), logMessage
                    .getMessage());
        } else {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(logMessage.getFields())) {
                logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString()),
                        logMessage.getMessage());
            }
        }
    }
}
