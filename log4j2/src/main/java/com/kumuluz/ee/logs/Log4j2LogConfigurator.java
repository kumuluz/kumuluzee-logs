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

import com.kumuluz.ee.logs.utils.Log4j2LogUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Rok Povse
 * @author Marko Skrjanec
 */
public class Log4j2LogConfigurator implements LogConfigurator {

    private static final Logger LOG = com.kumuluz.ee.logs.LogManager.getLogger(Log4j2LogConfigurator.class
            .getSimpleName());

    private static final String DEFAULT_CONFIG = "META-INF/kumuluzee/log4j2-default.xml";

    @Override
    public void setLevel(String logName, String logLevel) {

        logLevel = logLevel.trim().toUpperCase();

        try {
            Level level = Log4j2LogUtil.convertToLog4j2Level(logLevel);

            if (level != null) {
                Configurator.setLevel(logName.trim(), level);
            } else {
                LOG.error("Log4j2 logger level with value" + logLevel + " not defined");
            }
        } catch (Exception exception) {
            LOG.error("An error occurred when trying to set logger level.", exception);
        }
    }

    @Override
    public String getLevel(String logName) {
        return LogManager.getLogger(logName.trim()).getLevel().name();
    }

    @Override
    public void configure() {

        Configuration configuration = LoggerContext.getContext().getConfiguration();

        if (configuration instanceof DefaultConfiguration) {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG);

            configure(inputStream);
        }
    }

    @Override
    public void configure(String config) {
        configure(new ByteArrayInputStream(config.getBytes()));
    }

    @Override
    public void configure(Path file) {

        try {
            configure(Files.newInputStream(file));
        } catch (IOException exception) {
            LOG.error("An error occurred when trying to read configuration file.", exception);
        }
    }

    @Override
    public void configure(InputStream inputStream) {

        try {
            ConfigurationSource source = new ConfigurationSource(inputStream);
            XmlConfiguration config = new XmlConfiguration(Configurator.initialize(null, source), source);
            LoggerContext.getContext(false).start(config);
        } catch (Exception exception) {
            LOG.error("An error occurred when trying to read Log4j2 configuration.", exception);
        }
    }
}
