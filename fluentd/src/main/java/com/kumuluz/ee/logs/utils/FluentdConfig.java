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

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

/**
 * @author Domen Gašperlin
 * @since 1.4.0
 */
public class FluentdConfig {

    private String hostname;
    private int port;

    // Log context
    private String environmentType;
    private String applicationName;
    private String applicationVersion;

    private static FluentdConfig fluentdConfig;

    private static void initialize() {

        if (fluentdConfig != null) {
            throw new IllegalStateException("The FluentdConfig was already initialized.");
        }

        fluentdConfig = new FluentdConfig();

        ConfigurationUtil configurationUtil = ConfigurationUtil.getInstance();
        fluentdConfig.hostname = configurationUtil.get("kumuluzee.logs.fluentd.hostname").orElse("localhost");
        fluentdConfig.port = Integer.valueOf(configurationUtil.get("kumuluzee.logs.fluentd.port").orElse("24224"));

        fluentdConfig.environmentType = EeConfig.getInstance().getEnv().getName();

        fluentdConfig.applicationName = EeConfig.getInstance().getName();
        fluentdConfig.applicationVersion = EeConfig.getInstance().getVersion();

    }

    public synchronized static FluentdConfig getInstance() {

        if (fluentdConfig == null) {
            initialize();
        }

        return fluentdConfig;
    }

    public String getHostname() {
        return hostname;
    }

    public Integer getPort() {
        return port;
    }

    public String getEnvironmentType() {
        return environmentType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getUniqueInstanceId() {
        return EeRuntime.getInstance().getInstanceId();
    }

}
