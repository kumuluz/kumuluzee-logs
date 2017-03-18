/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs.interceptors;

import com.kumuluz.ee.logs.annotations.Log;
import com.kumuluz.ee.logs.messages.SimpleLogMessage;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Rok Povse, Marko Skrjanec
 */
@Log
@Interceptor
public class LogInterceptor {

    @AroundInvoke
    public Object manageTransaction(InvocationContext context) throws Exception {

        // Log log = context.getMethod().getDeclaredAnnotation(Log.class);
        // LogParams[] value = log.value();
        // boolean methodCall = log.methodCall();

        SimpleLogMessage message = new SimpleLogMessage();
        message.setMessage("Entering method.");

        Map<String, String> map = new HashMap<String, String>();
        map.put("class", context.getTarget().getClass().getSuperclass().getName());
        map.put("method", context.getMethod().getName());
        message.setFields(map);

        Object result = context.proceed();

        return result;
    }
}