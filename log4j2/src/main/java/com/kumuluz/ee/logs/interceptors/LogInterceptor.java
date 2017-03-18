/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs.interceptors;

import com.kumuluz.ee.logs.LogCommons;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.annotations.Log;
import com.kumuluz.ee.logs.types.LogMethodContext;
import com.kumuluz.ee.logs.types.LogMethodMessage;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;


/**
 * @Author Rok Povse, Marko Skrjanec
 */
@Log
@Interceptor
public class LogInterceptor {

    @AroundInvoke
    public Object manageTransaction(InvocationContext context) throws Exception {

        LogCommons logger = LogManager.getCommonsLogger(context.getTarget().getClass().getSuperclass().getName());
        LogMethodMessage message = new LogMethodMessage();

        LogMethodContext logMethodContext = logger.logMethodEntry(message);

        Object result = context.proceed();

        logger.logMethodExit(logMethodContext);
        return result;
    }
}