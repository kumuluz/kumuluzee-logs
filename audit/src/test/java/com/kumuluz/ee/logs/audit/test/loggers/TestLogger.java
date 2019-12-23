/*
 *  Copyright (c) 2014-2019 Kumuluz and/or its affiliates
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
package com.kumuluz.ee.logs.audit.test.loggers;

import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.messages.LogMessage;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Gregor Porocnik
 */
public class TestLogger implements Logger {

    private static Logger logger = new TestLogger();

    private static List<LogMessage> messages = new LinkedList<>();

    public static List<LogMessage> getMessages() {
        return messages;
    }

    public static void setMessages(List<LogMessage> messages) {
        TestLogger.messages = messages;
    }

    @Override
    public Logger getLogger(String logName) {
        return logger;
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
        messages.add(message);
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
