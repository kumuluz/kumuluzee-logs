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

import com.kumuluz.ee.common.LogsExtension;
import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.dependencies.EeComponentDependency;
import com.kumuluz.ee.common.dependencies.EeComponentType;
import com.kumuluz.ee.common.dependencies.EeExtensionDef;
import com.kumuluz.ee.common.dependencies.EeExtensionGroup;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.enums.LogLevel;

import java.io.File;
import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * KumuluzEE framework extension for log4j2-based logging
 *
 * @author Jan Meznarič
 * @since 1.2.0
 */
@EeExtensionDef(name = "log4j2", group = EeExtensionGroup.LOGS)
@EeComponentDependency(EeComponentType.SERVLET)
public class Log4j2LogExtension implements LogsExtension {

    @Override
    public void load() {
    }

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {
        initConfiguration();
        initWatchers();
    }

    @Override
    public Optional<Class<? extends LogManager>> getJavaUtilLogManagerClass() {
        return Optional.of(org.apache.logging.log4j.jul.LogManager.class);
    }

    @Override
    public Optional<Handler> getJavaUtilLogHandlerClass() {
        return Optional.empty();
    }

    /**
     * Helper method for initiating configuration.
     */
    private void initConfiguration() {
        ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();

        // Init configuration
        if (configurationUtil.get("kumuluzee.logs.config-file").isPresent()) {
            System.out.println("kumuluzee.logs.config-file: " + configurationUtil.get("kumuluzee.logs.config-file")
                    .get());
            LogUtil.getInstance().getLogConfigurator()
                    .configure(configurationUtil.get("kumuluzee.logs.config-file").get());
        } else if (configurationUtil.get("kumuluzee.logs.config-file-location").isPresent()) {
            System.out.println("kumuluzee.logs.config-file-location: " + configurationUtil.get("kumuluzee.logs" +
                    ".config-file-location").get());
            LogUtil.getInstance().getLogConfigurator()
                    .configure(new File(configurationUtil.get("kumuluzee.logs.config-file-location").get()));
        }

        final String loggers = "kumuluzee.logs.loggers";
        if (configurationUtil.getListSize(loggers).isPresent()) {
            System.out.println(loggers);
            initLoggers(loggers);
        }

        if (EeConfig.getInstance().getDebug()) {
            LogUtil.getInstance().getLogConfigurator().setLevel("", LogLevel.DEBUG.toString());
        }
    }

    /**
     * Helper method for initiating watchers.
     */
    private void initWatchers() {
        ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();

        final String configFile = "kumuluzee.logs.config-file";
        ConfigurationUtil.getInstance().subscribe(configFile, (String key, String value) -> {
            if (configFile.equals(key)) {
                System.out.println(configFile + ": " + value);
                LogUtil.getInstance().getLogConfigurator().configure(value);
            }
        });

        final String configFileLocation = "kumuluzee.logs.config-file-location";
        ConfigurationUtil.getInstance().subscribe(configFileLocation, (String key, String value) -> {
            if (configFileLocation.equals(key)) {
                System.out.println(configFileLocation + ": " + value);
                LogUtil.getInstance().getLogConfigurator().configure(new File(value));
            }
        });

        final String loggers = "kumuluzee.logs.loggers";
        ConfigurationUtil.getInstance().subscribe(loggers, (String key, String value) -> {
            if (key != null && key.startsWith(loggers)) {
                initLoggers(loggers);
            }
        });

        final String debug = "kumuluzee.debug";
        ConfigurationUtil.getInstance().subscribe(debug, (String key, String value) -> {
            if (debug.equals(key)) {
                System.out.println(debug + ": " + value);
                if ("true".equals(value)) {
                    LogUtil.getInstance().getLogConfigurator().setLevel("", LogLevel.DEBUG.toString());
                } else if ("false".equals(value)) {
                    initConfiguration();
                }
            }
        });
    }

    /**
     * Helper method for initiating loggers.
     */
    private void initLoggers(String loggers) {
        ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();

        int length = configurationUtil.getListSize(loggers).get();

        for (int i = 0; i < length; i++) {
            if (ConfigurationUtil.getInstance().get(loggers + "[" + i + "].name").isPresent() &&
                    ConfigurationUtil.getInstance().get(loggers + "[" + i + "].level").isPresent()) {

                System.out.println(ConfigurationUtil.getInstance().get(loggers + "[" + i + "].name").get() + ":"
                        + ConfigurationUtil.getInstance().get(loggers + "[" + i + "].level").get());

                LogUtil.getInstance().getLogConfigurator().setLevel(
                        ConfigurationUtil.getInstance().get(loggers + "[" + i + "].name").get(),
                        ConfigurationUtil.getInstance().get(loggers + "[" + i + "].level").get());
            }
        }
    }
}
