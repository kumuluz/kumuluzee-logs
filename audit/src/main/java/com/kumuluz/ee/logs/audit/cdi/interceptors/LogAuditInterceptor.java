package com.kumuluz.ee.logs.audit.cdi.interceptors;

/*
 *  Copyright (c) 2014-2019 Kumuluz and/or its affiliates
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

import com.kumuluz.ee.common.utils.StringUtils;
import com.kumuluz.ee.logs.audit.AuditLogUtil;
import com.kumuluz.ee.logs.audit.annotations.AuditProperty;
import com.kumuluz.ee.logs.audit.annotations.LogAudit;
import com.kumuluz.ee.logs.audit.cdi.AuditLog;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;

/**
 * @author Gregor Porocnik
 */
@LogAudit
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 1)
public class LogAuditInterceptor {

    @Inject
    private AuditLog auditLog;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {

        final Method method = context.getMethod();

        if (method.getDeclaringClass().getAnnotation(Path.class) != null) {
            //API resource method, annotations are handled by filter
            return context.proceed();
        }

        LogAudit logAuditMethodAnnotation = method.getDeclaredAnnotation(LogAudit.class);
        LogAudit logAuditAnnotation = logAuditMethodAnnotation != null ? logAuditMethodAnnotation : method.getDeclaringClass().getDeclaredAnnotation(LogAudit.class);

        if (logAuditAnnotation == null) {
            return context.proceed();
        }

        final String actionName = logAuditAnnotation.name();
        final String action = StringUtils.isNullOrEmpty(actionName) ? method.getName() : actionName;
        final AuditProperty[] auditPropertyAnnotations = logAuditAnnotation.properties();
        Set<com.kumuluz.ee.logs.audit.types.AuditProperty> auditProperties = AuditLogUtil.getAuditProperties(auditPropertyAnnotations);

        Object[] parameterValues = context.getParameters();
        Parameter[] methodParameters = method.getParameters();

        //method parameter annotated values
        auditProperties.addAll(AuditLogUtil.getAuditProperties(methodParameters, parameterValues));

        auditLog.log(action, auditProperties.stream().toArray(com.kumuluz.ee.logs.audit.types.AuditProperty[]::new));

        return context.proceed();
    }

}

