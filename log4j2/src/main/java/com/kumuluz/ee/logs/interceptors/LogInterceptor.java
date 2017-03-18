/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs.interceptors;

import com.kumuluz.ee.logs.LogCommons;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.annotations.Log;
import com.kumuluz.ee.logs.annotations.enums.LogParams;
import com.kumuluz.ee.logs.messages.SimpleLogMessage;
import com.kumuluz.ee.logs.types.LogMethodContext;
import com.kumuluz.ee.logs.types.LogMethodMessage;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;


/**
 * @Author Rok Povse, Marko Skrjanec
 */
@Log
@Interceptor
public class LogInterceptor {

    @AroundInvoke
    public Object logMethodEntryAndExit(InvocationContext context) throws Exception {

        // get annotation either from class or method
        Log annotation = context.getMethod().getDeclaredAnnotation(Log.class) != null ? context.getMethod()
                .getDeclaredAnnotation(Log.class) : context.getMethod().getDeclaringClass().getDeclaredAnnotation(Log
                .class);

        // get annotation params
        LogParams[] value = annotation.value();
        boolean methodCall = annotation.methodCall();

        // get logger
        LogCommons logger = LogManager.getCommonsLogger(context.getTarget().getClass().getSuperclass().getName());

        // set message
        LogMethodMessage message = new LogMethodMessage();

        // set metrics
        for (LogParams logParam : value) {
            if (LogParams.METRICS.equals(logParam)) {
                message.enableMetrics();
                break;
            }
        }

        SimpleLogMessage msg = new SimpleLogMessage();
        msg.setMessage("Entering method.");
        msg.setFields(new HashMap<String, String>());

        // set method call
        if (methodCall) {
            msg.getFields().put("class", context.getMethod().getDeclaringClass().getName());
            msg.getFields().put("method", context.getMethod().getName());
        }
        message.enableCall(msg);

        // log entry
        LogMethodContext logMethodContext = logger.logMethodEntry(message);

        Object result = context.proceed();

        // set method call
        msg.setMessage("Exiting method.");
        logMethodContext.setCallExitMessage(msg);

        // log exit
        logger.logMethodExit(logMethodContext);

        return result;
    }
}