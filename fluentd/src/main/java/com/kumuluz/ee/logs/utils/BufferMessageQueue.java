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

import com.kumuluz.ee.logs.dtos.LogRecord;
import org.fluentd.logger.FluentLogger;

import java.util.HashMap;
import java.util.List;

/**
 * @author Domen Gašperlin
 * @since 1.4.0
 */
public class BufferMessageQueue {

    private List<LogRecord> logs;
    private Boolean buffer;

    private static BufferMessageQueue bufferMessageQueue;

    private BufferMessageQueue() {
    }

    private static void initialize() {

        if (bufferMessageQueue != null) {
            throw new IllegalStateException("BufferMessageQueue was already initialized.");
        }

        bufferMessageQueue = new BufferMessageQueue();

        setBufferMessageQueue(true);


    }

    public synchronized static BufferMessageQueue getInstance() {

        if (bufferMessageQueue == null) {
            initialize();
        }

        return bufferMessageQueue;
    }


    public synchronized void addBuffer(HashMap<String, Object> data, Long timestamp, FluentLogger logger) {
        if (bufferMessageQueue.buffer) {
            getLogs().add(new LogRecord(data, timestamp, logger));
        }
    }

    public synchronized void flushBuffer(HashMap<String, Object> contextData) {
        if (bufferMessageQueue.buffer) {
            for (LogRecord logRecord : getLogs()) {
                FluentLogger recordLogger = logRecord.getLogger();
                HashMap<String, Object> record = logRecord.getRecord();
                record.putAll(contextData);
                recordLogger.log(recordLogger.getName(), record, logRecord.getTimestamp());
            }
            bufferMessageQueue.buffer = false;
            getLogs().clear();
        }

    }

    public Boolean getBuffer() {
        return bufferMessageQueue.buffer;
    }

    public void setBuffer(Boolean buffer) {
        bufferMessageQueue.buffer = buffer;
    }

    public void setLogs(List<LogRecord> logs) {
        bufferMessageQueue.logs = logs;
    }

    public static void setBufferMessageQueue(Boolean buffer) {
        bufferMessageQueue.buffer = buffer;
    }

    public static List<LogRecord> getLogs() {
        return bufferMessageQueue.logs;
    }
}
