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

import java.io.InputStream;
import java.nio.file.Path;
import java.util.logging.LogManager;

/**
 * @author Domen Gašperlin
 * @since 1.4.0
 */
public class FluentdLogConfigurator implements LogConfigurator {

    private static final Logger LOG = com.kumuluz.ee.logs.LogManager
            .getLogger(FluentdLogConfigurator.class.getName());


    @Override
    public void setLevel(String logName, String logLevel) {
    }

    @Override
    public String getLevel(String logName) {
        return LogManager.getLogManager().getLogger(logName.trim()).getLevel().getName();
    }

    @Override
    public void configure() {
    }

    @Override
    public void configure(String config) {
    }

    @Override
    public void configure(Path config) {
    }

    @Override
    public void configure(InputStream config) {
    }
}
