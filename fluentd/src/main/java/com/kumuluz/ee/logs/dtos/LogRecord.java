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

package com.kumuluz.ee.logs.dtos;

import org.fluentd.logger.FluentLogger;

import java.util.HashMap;

/**
 * @author Domen Gašperlin
 * @since 1.4.0
 */
public class LogRecord {

    private HashMap<String, Object> record;
    private Long timestamp;
    private FluentLogger logger;

    public LogRecord(HashMap<String, Object> record, Long timestamp, FluentLogger logger) {
        this.record = record;
        this.timestamp = timestamp;
        this.logger = logger;
    }

    public HashMap<String, Object> getRecord() {
        return record;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public FluentLogger getLogger() {
        return logger;
    }
}
