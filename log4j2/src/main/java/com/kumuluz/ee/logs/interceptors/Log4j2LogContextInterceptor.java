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
package com.kumuluz.ee.logs.interceptors;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.messages.SimpleLogMessage;
import org.apache.logging.log4j.CloseableThreadContext;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;

/**
 * Kumuluz-logs interceptor for setting ThreadContext fields.
 *
 * @author Rok Povse, Marko Skrjanec
 */
@Log
@Interceptor
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
public class Log4j2LogContextInterceptor {

    @AroundInvoke
    public Object logMethodEntryAndExit(InvocationContext context) throws Exception {

        SimpleLogMessage msg = new SimpleLogMessage();
        msg.setFields(new HashMap<String, String>());

        if (EeConfig.getInstance().getName() != null) {
            msg.getFields().put("name", EeConfig.getInstance().getName());
        }
        if (EeConfig.getInstance().getVersion() != null) {
            msg.getFields().put("version", EeConfig.getInstance().getVersion());
        }
        if (EeConfig.getInstance().getEnv().getName() != null) {
            msg.getFields().put("env-name", EeConfig.getInstance().getEnv().getName());
        }

        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(msg.getFields())) {
            Object result = context.proceed();
            return result;
        }
    }
}
