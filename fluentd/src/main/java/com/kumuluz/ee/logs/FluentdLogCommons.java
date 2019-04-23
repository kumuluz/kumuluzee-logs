/*
 *  Copyright (c) 2014-2018 Kumuluz and/or its affiliates
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

import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.markers.CommonsMarker;
import com.kumuluz.ee.logs.markers.Marker;
import com.kumuluz.ee.logs.markers.StatusMarker;
import com.kumuluz.ee.logs.messages.LogMessage;
import com.kumuluz.ee.logs.messages.SimpleLogMessage;
import com.kumuluz.ee.logs.types.LogMethodContext;
import com.kumuluz.ee.logs.types.LogMethodMessage;
import com.kumuluz.ee.logs.types.LogResourceContext;
import com.kumuluz.ee.logs.types.LogResourceMessage;
import com.kumuluz.ee.logs.utils.BufferMessageQueue;
import com.kumuluz.ee.logs.utils.FluentdConfig;
import com.kumuluz.ee.logs.utils.FluentdLogUtil;

import java.util.HashMap;
import java.util.ServiceLoader;

/**
 * @author Domen Gašperlin
 * @since 1.4.0
 */
public class FluentdLogCommons implements LogCommons {

    private static final String METRIC_RESPONSE_TIME = "response-time";

    private static LogLevel DEFAULT_LOG_LEVEL = LogLevel.TRACE;

    private org.fluentd.logger.FluentLogger logger;

    private FluentdLogCommons(String logName) {

        FluentdConfig fluentdConfig = FluentdConfig.getInstance();
        logger = org.fluentd.logger.FluentLogger.getLogger(
                logName,
                fluentdConfig.getHostname(),
                fluentdConfig.getPort());
    }

    public FluentdLogCommons() {

    }

    @Override
    public LogCommons getCommonsLogger(String logName) {
        return new FluentdLogCommons(logName);
    }

    @Override
    public String getName() {
        return logger.getName();
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
    public LogMethodContext logMethodEntry(LogLevel level, Marker marker, LogMethodMessage logMethodMessage) {
        log(level, StatusMarker.ENTRY, marker, logMethodMessage.getCallMessage());
        return new LogMethodContext(logMethodMessage, level, marker);
    }

    @Override
    public void logMethodExit(LogMethodContext logMethodContext) {
        if (logMethodContext.isMetricsEnabled() != null && logMethodContext.isMetricsEnabled()) {
            if (logMethodContext.getCallExitMessage() == null) {
                logMethodContext.setCallExitMessage(new SimpleLogMessage());
            }
            logMethodContext.getCallExitMessage().getFields().put(METRIC_RESPONSE_TIME,
                    logMethodContext.getLogMetrics().getTimeElapsed().toString());

            logExit(logMethodContext);
        } else {
            logExit(logMethodContext);
        }
    }

    @Override
    public LogResourceContext logResourceStart(LogResourceMessage logResourceMessage) {
        return logResourceStart(CommonsMarker.RESOURCE, logResourceMessage);

    }

    @Override
    public LogResourceContext logResourceStart(LogLevel level, LogResourceMessage logResourceMessage) {
        return logResourceStart(level, CommonsMarker.RESOURCE, logResourceMessage);
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
            if (logResourceContext.getInvokeEndMessage() == null) {
                SimpleLogMessage message = new SimpleLogMessage();
                message.setFields(new HashMap<>());
                logResourceContext.setInvokeEndMessage(message);
            }

            logResourceContext.getInvokeEndMessage().getFields().put(METRIC_RESPONSE_TIME,
                    logResourceContext.getLogMetrics().getTimeElapsed().toString());

            logEnd(logResourceContext);
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

        String markerString = parentMarker == null ? marker.toString() : marker + "[ " + parentMarker + " ]";
        HashMap<String, Object> context = new HashMap<>();

        context = addContextData(context);

        if (logMessage != null && logMessage.getFields() != null) {
            context.put("level", FluentdLogUtil.convertToFluentdLevel(level).getName());
            context.put("message", markerString + " " + message + " " +
                    logMessage.getFields());

        } else {
            context.put("level", FluentdLogUtil.convertToFluentdLevel(level).getName());
            context.put("message", markerString + " " + message);

        }
        BufferMessageQueue bufferMessageQueue = BufferMessageQueue.getInstance();
        if (bufferMessageQueue.getBuffer()) {
            bufferMessageQueue.addBuffer(context, System.currentTimeMillis(), logger);
        } else {
            bufferMessageQueue.flushBuffer(context);
            System.out.println(logger.getName() + " " + context.toString());
            logger.log(logger.getName(), context);
        }

    }

    /**
     * @param data defines input data
     * @return data with appended context data
     */
    private HashMap<String, Object> addContextData(HashMap<String, Object> data) {

        // FluentdConfig data
        FluentdConfig fluentdConfig = FluentdConfig.getInstance();

        data.put("environmentType", fluentdConfig.getEnvironmentType());
        data.put("applicationName", fluentdConfig.getApplicationName());
        data.put("applicationVersion", fluentdConfig.getApplicationVersion());
        data.put("uniqueInstanceId", fluentdConfig.getUniqueInstanceId());

        ServiceLoader.load(RequestContext.class).forEach(provider -> {
            HashMap<String, String> context = provider.getContext();
            if (context != null) {
                data.putAll(context);
            }
        });

        return data;
    }

}
