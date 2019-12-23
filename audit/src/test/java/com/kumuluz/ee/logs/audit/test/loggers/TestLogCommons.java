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

import com.kumuluz.ee.logs.LogCommons;
import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.markers.Marker;
import com.kumuluz.ee.logs.types.LogMethodContext;
import com.kumuluz.ee.logs.types.LogMethodMessage;
import com.kumuluz.ee.logs.types.LogResourceContext;
import com.kumuluz.ee.logs.types.LogResourceMessage;

/**
 * @author Gregor Porocnik
 */
public class TestLogCommons implements LogCommons {

    @Override
    public LogCommons getCommonsLogger(String logName) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setDefaultLevel(LogLevel logLevel) {

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
    public LogMethodContext logMethodEntry(Marker marker, LogMethodMessage logMethodMessage) {
        return null;
    }

    @Override
    public LogMethodContext logMethodEntry(LogLevel level, Marker marker, LogMethodMessage logMethodMessage) {
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
    public LogResourceContext logResourceStart(LogLevel level, LogResourceMessage logResourceMessage) {
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
}
