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
import com.kumuluz.ee.common.dependencies.*;
import com.kumuluz.ee.common.wrapper.KumuluzServerWrapper;

import java.util.Optional;
import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * KumuluzEE framework extension for jul-based logging
 *
 * @author Marko Skrjanec
 * @since 1.4.0
 */
@EeExtensionDef(name = "jul", group = EeExtensionGroup.LOGS)
@EeComponentDependencies({
        @EeComponentDependency(EeComponentType.SERVLET),
        @EeComponentDependency(EeComponentType.CDI)
})
public class JavaUtilLogExtension implements LogsExtension {

    private LogDeferrer<Logger> logDeferrer;

    @Override
    public void load() {

        logDeferrer = new LogDeferrer<>();

        LogInitializationUtil.loadConfiguration(logDeferrer);
    }

    @Override
    public void init(KumuluzServerWrapper kumuluzServerWrapper, EeConfig eeConfig) {

        logDeferrer.execute();
        logDeferrer = null;

        LogInitializationUtil.initWatchers();
    }

    @Override
    public Optional<Class<? extends LogManager>> getJavaUtilLogManagerClass() {
        return Optional.empty();
    }

    @Override
    public Optional<Handler> getJavaUtilLogHandler() {
        return Optional.empty();
    }
}
