/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.kumuluz.ee.logs;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
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

import java.util.HashMap;
import java.util.ServiceLoader;

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

        return new Log4j2LogCommons(logName);
    }

    @Override
    public void setDefaultLevel(LogLevel logLevel) {
        DEFAULT_LOG_LEVEL = logLevel;
    }

    @Override
    public LogMethodContext logMethodEntry(LogMethodMessage logMethodMessage) {
        return logMethodEntry(DEFAULT_LOG_LEVEL, CommonsMarker.METHOD, logMethodMessage);
    }

    @Override
    public LogMethodContext logMethodEntry(LogLevel level, LogMethodMessage logMethodMessage) {
        return logMethodEntry(level, CommonsMarker.METHOD, logMethodMessage);
    }

    @Override
    public LogMethodContext logMethodEntry(Marker marker, LogMethodMessage logMethodMessage) {
        return logMethodEntry(DEFAULT_LOG_LEVEL, marker, logMethodMessage);
    }

    @Override
    public LogMethodContext logMethodEntry(LogLevel logLevel, Marker marker, LogMethodMessage logMethodMessage) {
        log(logLevel, StatusMarker.ENTRY, marker, logMethodMessage.getCallMessage());
        return new LogMethodContext(logMethodMessage, logLevel, marker);
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
    public LogResourceContext logResourceStart(LogLevel logLevel, LogResourceMessage logResourceMessage) {
        return logResourceStart(logLevel, CommonsMarker.RESOURCE, logResourceMessage);
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
        if (logMethodContext.getMarker() != null) {
            log(logMethodContext.getLevel(), StatusMarker.EXIT, logMethodContext.getMarker(), logMethodContext
                    .getCallExitMessage());
        } else {
            log(logMethodContext.getLevel(), StatusMarker.EXIT, CommonsMarker.METHOD, logMethodContext
                    .getCallExitMessage());
        }
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

        String message;
        if (logMessage == null || logMessage.getMessage() == null) {
            message = "";
        } else {
            message = logMessage.getMessage();
        }

        if (logMessage != null && logMessage.getFields() != null) {
            CloseableThreadContext.putAll(getRequestContextData());
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(logMessage.getFields())) {
                logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString())
                        .setParents(MarkerManager.getMarker(parentMarker.toString())), message);
            }
        } else {
            logger.log(Log4j2LogUtil.convertToLog4j2Level(level), MarkerManager.getMarker(marker.toString())
                    .setParents(MarkerManager.getMarker(parentMarker.toString())), message);

        }
    }

    /**
     * @return request context data from external RequestContext objects
     */
    private HashMap<String, String> getRequestContextData() {

        EeConfig eeConfig = EeConfig.getInstance();

        HashMap<String, String> requestContextData = new HashMap<>();

        requestContextData.put("environmentType", eeConfig.getEnv().getName());
        requestContextData.put("applicationName", eeConfig.getName());
        requestContextData.put("applicationVersion", eeConfig.getVersion());
        requestContextData.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());

        ServiceLoader.load(RequestContext.class).forEach(provider -> {
            HashMap<String, String> context = provider.getContext();
            if (context != null) {
                requestContextData.putAll(context);
            }
        });

        return requestContextData;
    }
}
