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

package com.kumuluz.ee.logs.utils;

import org.fluentd.logger.FluentLogger;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Domen Gašperlin
 * @since 1.4.0
 */
public class LoggersQueue {

    private HashMap<String, FluentLogger> queue;

    private static LoggersQueue loggersQueue;

    private static void initialize() {

        if (loggersQueue != null) {
            throw new IllegalStateException("The LoggersQueue was already initialized.");
        }

        setLoggersQueue(new LoggersQueue());

        setQueue(new HashMap<>());
    }

    public synchronized static LoggersQueue getInstance() {

        if (loggersQueue == null) {
            initialize();
        }

        return loggersQueue;
    }


    public void put(String key, FluentLogger fluentLogger) {
        loggersQueue.getQueue().put(key, fluentLogger);
        fluentLogger.log(fluentLogger.getName(), "message", "Connection to fluentd daemon initialized");
    }


    public void closeLoggerConnections() {

        HashMap<String, FluentLogger> queue = loggersQueue.getQueue();
        Iterator it = queue.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry e = (HashMap.Entry) it.next();
            FluentLogger fluentLogger = ((FluentLogger) e.getValue());
            fluentLogger.log(fluentLogger.getName(), "message", "Connection to fluentd daemon closed");
        }

        loggersQueue.getQueue().clear();
    }

    public static void setQueue(HashMap<String, FluentLogger> queue) {
        loggersQueue.queue = queue;
    }

    public HashMap<String, FluentLogger> getQueue() {
        return loggersQueue.queue;
    }

    public static void setLoggersQueue(LoggersQueue loggersQueue) {
        LoggersQueue.loggersQueue = loggersQueue;
    }
}
