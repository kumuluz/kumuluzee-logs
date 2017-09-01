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

import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.utils.JavaUtilLogUtil;

import java.io.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * @Author Marko Skrjanec
 */
public class JavaUtilLogConfigurator implements LogConfigurator {

    private static final Logger LOG = com.kumuluz.ee.logs.LogManager.getLogger(JavaUtilLogConfigurator.class
            .getSimpleName());

    @Override
    public void setLevel(String logName, String logLevel) {
        try {
            Level level = JavaUtilLogUtil.convertToJULLevel(logLevel);
            if (level != null) {
                LogManager.getLogManager().getLogger(logName).setLevel(level);
            } else {
                LOG.error("JUL logger level with value=" + logLevel + " not defined");
            }

        } catch (Exception exception) {
            LOG.error("An error occurred when trying to set logger level.", exception);
        }
    }

    @Override
    public String getLevel(String logName) {
        return LogManager.getLogManager().getLogger(logName).getLevel().getName();
    }

    @Override
    public void setDebug(boolean debug) {
        if (debug) {
            Level level = JavaUtilLogUtil.convertToJULLevel(LogLevel.DEBUG);
            java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
            rootLogger.setLevel(level);
            for (Handler handler : rootLogger.getHandlers()) {
                handler.setLevel(level);
            }
        } else {
            LogInitializationUtil.initConfiguration();
        }
    }

    @Override
    public void configure() {
        try {
            LogManager.getLogManager().readConfiguration();
        } catch (Exception e) {
            LOG.error("An error occurred when trying to read default configuration file.");
        }
    }

    @Override
    public void configure(String config) {
        configure(new ByteArrayInputStream(config.getBytes()));
    }

    @Override
    public void configure(File file) {
        try {
            configure(new FileInputStream(file));
        } catch (FileNotFoundException exception) {
            LOG.error("An error occurred when trying to read configuration file.", exception);
        }
    }

    @Override
    public void configure(InputStream inputStream) {
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (Exception exception) {
            LOG.error("An error occurred when trying to read Log4j2 configuration.", exception);
        }
    }
}
