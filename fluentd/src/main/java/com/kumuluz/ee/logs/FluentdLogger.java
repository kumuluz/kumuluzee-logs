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
import com.kumuluz.ee.logs.messages.LogMessage;
import com.kumuluz.ee.logs.utils.FluentdConfig;
import com.kumuluz.ee.logs.utils.FluentdLogUtil;
import com.kumuluz.ee.logs.utils.LoggersQueue;

import java.util.HashMap;

/**
 * @author Domen Gašperlin
 * @since 1.4.0
 */
public class FluentdLogger implements Logger {

    private org.fluentd.logger.FluentLogger logger;

    private java.util.logging.Logger log = java.util.logging.Logger.getLogger(FluentdLogger.class.getSimpleName());

    private LoggersQueue loggersQueue = LoggersQueue.getInstance();


    public FluentdLogger(String logName) {

        FluentdConfig fluentdConfig = FluentdConfig.getInstance();
        logger = org.fluentd.logger.FluentLogger.getLogger(logName, fluentdConfig.getHostname(),
                fluentdConfig.getPort());
        loggersQueue.put(logName, logger);

    }

    public FluentdLogger() {

    }

    @Override
    public Logger getLogger(String logName) {
        return new FluentdLogger(logName);
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void log(LogLevel level, String message) {

        addFieldsAndLog(level, message, null, null);

    }

    @Override
    public void log(LogLevel level, String message, Object... args) {

        addFieldsAndLog(level, message, null, args);

    }

    @Override
    public void log(LogLevel level, String message, Throwable thrown) {

        addFieldsAndLog(level, message, thrown, null);

    }

    @Override
    public void log(LogLevel level, Throwable thrown) {

        addFieldsAndLog(level, null, thrown, null);

    }

    @Override
    public void log(LogLevel level, LogMessage message) {

        addFieldsAndLog(level, message.getMessage(), null, null);

    }

    @Override
    public void log(LogLevel level, LogMessage message, Throwable thrown) {

        addFieldsAndLog(level, message.getMessage(), thrown, null);

    }

    @Override
    public void trace(String message) {

        addFieldsAndLog(LogLevel.TRACE, message, null, null);

    }

    @Override
    public void trace(String message, Object... args) {

        addFieldsAndLog(LogLevel.TRACE, message, null, args);

    }

    @Override
    public void trace(String message, Throwable thrown) {

        addFieldsAndLog(LogLevel.TRACE, message, thrown, null);

    }

    @Override
    public void trace(Throwable thrown) {

        addFieldsAndLog(LogLevel.TRACE, null, thrown, null);

    }

    @Override
    public void trace(LogMessage message) {

        addFieldsAndLog(LogLevel.TRACE, message.getMessage(), null, null);

    }

    @Override
    public void trace(LogMessage message, Throwable thrown) {

        addFieldsAndLog(LogLevel.TRACE, message.getMessage(), thrown, null);

    }

    @Override
    public void info(String message) {

        addFieldsAndLog(LogLevel.INFO, message, null, null);

    }

    @Override
    public void info(String message, Object... args) {

        addFieldsAndLog(LogLevel.INFO, message, null, args);

    }

    @Override
    public void info(String message, Throwable thrown) {

        addFieldsAndLog(LogLevel.INFO, message, thrown, null);

    }

    @Override
    public void info(Throwable thrown) {

        addFieldsAndLog(LogLevel.INFO, null, thrown, null);

    }

    @Override
    public void info(LogMessage message) {

        addFieldsAndLog(LogLevel.INFO, message.getMessage(), null, null);

    }

    @Override
    public void info(LogMessage message, Throwable thrown) {

        addFieldsAndLog(LogLevel.INFO, message.getMessage(), thrown, null);

    }

    @Override
    public void debug(String message) {

        addFieldsAndLog(LogLevel.DEBUG, message, null, null);

    }

    @Override
    public void debug(String message, Object... args) {

        addFieldsAndLog(LogLevel.DEBUG, message, null, args);

    }

    @Override
    public void debug(String message, Throwable thrown) {

        addFieldsAndLog(LogLevel.DEBUG, message, thrown, null);

    }

    @Override
    public void debug(Throwable thrown) {

        addFieldsAndLog(LogLevel.DEBUG, null, thrown, null);

    }

    @Override
    public void debug(LogMessage message) {

        addFieldsAndLog(LogLevel.DEBUG, message.getMessage(), null, null);

    }

    @Override
    public void debug(LogMessage message, Throwable thrown) {

        addFieldsAndLog(LogLevel.DEBUG, message.getMessage(), thrown, null);

    }

    @Override
    public void warn(String message) {

        addFieldsAndLog(LogLevel.WARN, message, null, null);

    }

    @Override
    public void warn(String message, Object... args) {

        addFieldsAndLog(LogLevel.WARN, message, null, args);

    }

    @Override
    public void warn(String message, Throwable thrown) {

        addFieldsAndLog(LogLevel.WARN, message, thrown, null);

    }

    @Override
    public void warn(Throwable thrown) {

        addFieldsAndLog(LogLevel.WARN, null, thrown, null);

    }

    @Override
    public void warn(LogMessage message) {

        addFieldsAndLog(LogLevel.WARN, message.getMessage(), null, null);

    }

    @Override
    public void warn(LogMessage message, Throwable thrown) {

        addFieldsAndLog(LogLevel.WARN, message.getMessage(), thrown, null);

    }

    @Override
    public void error(String message) {

        addFieldsAndLog(LogLevel.ERROR, message, null, null);

    }

    @Override
    public void error(String message, Object... args) {

        addFieldsAndLog(LogLevel.ERROR, message, null, args);

    }

    @Override
    public void error(String message, Throwable thrown) {

        addFieldsAndLog(LogLevel.ERROR, message, thrown, null);

    }

    @Override
    public void error(Throwable thrown) {

        addFieldsAndLog(LogLevel.ERROR, null, thrown, null);

    }

    @Override
    public void error(LogMessage message) {

        addFieldsAndLog(LogLevel.ERROR, message.getMessage(), null, null);

    }

    @Override
    public void error(LogMessage message, Throwable thrown) {

        addFieldsAndLog(LogLevel.ERROR, message.getMessage(), thrown, null);

    }

    private void addFieldsAndLog(LogLevel level, String message, Throwable thrown, Object... args) {

        HashMap<String, Object> data = new HashMap<>();

        data.put("level", FluentdLogUtil.convertToFluentdLevel(level).getName());

        if (message != null) {
            data.put("message", message);
        }

        if (thrown != null) {
            data.put("thrown", thrown);
        }

        if (args != null) {
            data.put("args", args);
        }

        log.log(FluentdLogUtil.convertToJULLevel(level), logger.getName() + " " + data);

        logger.log(logger.getName(), data);

    }

}
