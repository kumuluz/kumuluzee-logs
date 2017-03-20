/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.markers.CommonsMarker;
import com.kumuluz.ee.logs.markers.Marker;
import com.kumuluz.ee.logs.markers.StatusMarker;
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
        return logMethodEntry(DEFAULT_LOG_LEVEL, logMethodMessage);
    }

    @Override
    public LogMethodContext logMethodEntry(LogLevel level, LogMethodMessage logMethodMessage) {
        log(level, StatusMarker.ENTRY, CommonsMarker.METHOD, logMethodMessage.getCallMessage());
        return new LogMethodContext(logMethodMessage, level);
    }

    @Override
    public void logMethodExit(LogMethodContext logMethodContext) {
        if (logMethodContext.isMetricsEnabled() != null && logMethodContext.isMetricsEnabled()) {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put(METRIC_RESPONSE_TIME,
                    logMethodContext.getLogMetrics().getTimeElapsed().toString())) {
                logExit(logMethodContext);
            }
        } else {
            logExit(logMethodContext);
        }
    }

    @Override
    public LogResourceContext logResourceStart(LogResourceMessage logResourceMessage) {
        return logResourceStart(CommonsMarker.RESOURCE, logResourceMessage);
    }

    @Override
    public LogResourceContext logResourceStart(Marker marker, LogResourceMessage logResourceMessage) {
        return logResourceStart(DEFAULT_LOG_LEVEL, marker, logResourceMessage);
    }

    @Override
    public LogResourceContext logResourceStart(LogLevel level, Marker marker, LogResourceMessage logResourceMessage) {
        log(level, StatusMarker.INVOKE, marker, logResourceMessage.getInvokeMessage());
        return new LogResourceContext(logResourceMessage, level, marker);
    }

    @Override
    public void logResourceEnd(LogResourceContext logResourceContext) {
        if (logResourceContext.isMetricsEnabled() != null && logResourceContext.isMetricsEnabled()) {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put(METRIC_RESPONSE_TIME,
                    logResourceContext.getLogMetrics().getTimeElapsed().toString())) {
                logEnd(logResourceContext);
            }
        } else {
            logEnd(logResourceContext);
        }
    }

    /**
     * @param logMethodContext object defining LogMethodContext
     */
    private void logExit(LogMethodContext logMethodContext) {
        log(logMethodContext.getLevel(), StatusMarker.EXIT, CommonsMarker.METHOD, logMethodContext
                .getCallExitMessage());
    }

    /**
     * @param logResourceContext object defining LogResourceContext
     */
    private void logEnd(LogResourceContext logResourceContext) {
        log(logResourceContext.getLevel(), StatusMarker.RESPOND, logResourceContext.getMarker(), logResourceContext
                .getInvokeEndMessage());
    }

    /**
     * @param level        object defining Level
     * @param marker       object defining Marker
     * @param parentMarker object defining Marker
     * @param logMessage   object defining LogMessage
     */
    private void log(LogLevel level, Marker marker, Marker parentMarker, LogMessage logMessage) {
        if (logMessage == null || logMessage.getMessage() == null) {
            logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString())
                    .setParents(MarkerManager.getMarker(parentMarker.toString())), "");

        } else if (logMessage.getFields() == null) {
            logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString())
                    .setParents(MarkerManager.getMarker(parentMarker.toString())), logMessage.getMessage());
        } else {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(logMessage.getFields())) {
                logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString())
                        .setParents(MarkerManager.getMarker(parentMarker.toString())), logMessage.getMessage());
            }
        }
    }
}
