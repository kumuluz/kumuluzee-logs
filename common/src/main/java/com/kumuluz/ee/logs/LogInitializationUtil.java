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

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import java.io.File;

/**
 * @author Marko Skrjanec
 */
public class LogInitializationUtil {

    private static final String CONFIG_FILE_PATH = "kumuluzee.logs.config-file";
    private static final String CONFIG_FILE_LOCATION_PATH = "kumuluzee.logs.config-file-location";
    private static final String LOGGERS_PATH = "kumuluzee.logs.loggers";
    private static final String DEBUG_PATH = "kumuluzee.debug";

    private static final ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();

    private static final LogConfigurator logConfigurator = LogUtil.getInstance().getLogConfigurator();

    /**
     * Helper method for initiating configuration.
     */
    public static void initConfiguration() {
        if (configurationUtil.get(CONFIG_FILE_PATH).isPresent()) {
            logConfigurator.configure(configurationUtil.get(CONFIG_FILE_PATH).get());
        } else if (configurationUtil.get(CONFIG_FILE_LOCATION_PATH).isPresent()) {
            logConfigurator.configure(new File(configurationUtil.get(CONFIG_FILE_LOCATION_PATH).get()));
        }

        if (configurationUtil.getListSize(LOGGERS_PATH).isPresent()) {
            initLoggers();
        }

        if (configurationUtil.getBoolean(DEBUG_PATH).isPresent() && configurationUtil.getBoolean(DEBUG_PATH).get()) {
            logConfigurator.setDebug(true);
        }
    }

    /**
     * Helper method for initiating watchers.
     */
    public static void initWatchers() {
        ConfigurationUtil.getInstance().subscribe(CONFIG_FILE_PATH, (String key, String value) -> {
            if (CONFIG_FILE_PATH.equals(key)) {
                logConfigurator.configure(value);
            }
        });

        ConfigurationUtil.getInstance().subscribe(CONFIG_FILE_LOCATION_PATH, (String key, String value) -> {
            if (CONFIG_FILE_LOCATION_PATH.equals(key)) {
                logConfigurator.configure(new File(value));
            }
        });

        ConfigurationUtil.getInstance().subscribe(LOGGERS_PATH, (String key, String value) -> {
            if (key != null && key.startsWith(LOGGERS_PATH)) {
                initLoggers();
            }
        });

        ConfigurationUtil.getInstance().subscribe(DEBUG_PATH, (String key, String value) -> {
            if (DEBUG_PATH.equals(key)) {
                if ("true".equals(value.toLowerCase())) {
                    logConfigurator.setDebug(true);
                } else if ("false".equals(value.toLowerCase())) {
                    logConfigurator.setDebug(false);
                }
            }
        });
    }

    /**
     * Helper method for initiating loggers.
     */
    private static void initLoggers() {
        int length = configurationUtil.getListSize(LOGGERS_PATH).get();

        for (int i = 0; i < length; i++) {
            if (configurationUtil.get(LOGGERS_PATH + "[" + i + "].name").isPresent() &&
                    configurationUtil.get(LOGGERS_PATH + "[" + i + "].level").isPresent()) {

                LogUtil.getInstance().getLogConfigurator().setLevel(
                        configurationUtil.get(LOGGERS_PATH + "[" + i + "].name").get(),
                        configurationUtil.get(LOGGERS_PATH + "[" + i + "].level").get().toUpperCase());
            }
        }
    }
}
