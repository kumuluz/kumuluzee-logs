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
package com.kumuluz.ee.logs.audit.filters;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.audit.AuditLogUtil;
import com.kumuluz.ee.logs.audit.annotations.LogAudit;
import com.kumuluz.ee.logs.audit.cdi.AuditLog;
import com.kumuluz.ee.logs.audit.types.AuditProperty;
import com.kumuluz.ee.logs.audit.types.DataAuditAction;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

/**
 * @author Gregor Porocnik
 */
@Provider
@Priority(Priorities.USER - 1)
@ApplicationScoped
public class AuditLogFilter implements ContainerResponseFilter {

    @Inject
    private AuditLog auditLog;

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LogManager.getLogger(AuditLogFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext
            containerResponseContext) {

        try {
            final Method resourceMethod = resourceInfo.getResourceMethod();
            if (!auditLog.isEnabled() || resourceMethod == null) {
                return;
            }
            final Class<?> resourceClass = resourceInfo.getResourceClass();

            //parse from resource log audit annotation
            String name = AuditLogUtil.getLogAnnotatedFieldValue(resourceInfo.getResourceMethod().getAnnotation(LogAudit.class), LogAudit::name).orElseGet(resourceMethod::getName);
            String auditAction = AuditLogUtil.getLogAnnotatedFieldValue(resourceInfo, LogAudit::auditAction).orElseGet(() -> getAuditAction(resourceMethod));
            String objectType = AuditLogUtil.getLogAnnotatedFieldValue(resourceInfo, LogAudit::objectType).orElseGet(() -> getResourceObjectType(resourceClass));
            String objectIdentifier = DataAuditAction.CREATE.name().equals(auditAction) ? getUriCreatedIdentifier(containerResponseContext) : getResourceObjectIdentifier(resourceMethod, uriInfo);

            //method parameter annotated property values
            Parameter[] methodParameters = resourceInfo.getResourceMethod().getParameters();
            Set<AuditProperty> auditProperties = AuditLogUtil.getAuditProperties(methodParameters, containerRequestContext.getUriInfo());

            //method or class annotated property values
            Optional<com.kumuluz.ee.logs.audit.annotations.AuditProperty[]> methodAnnotatedProperties = AuditLogUtil.getLogAnnotatedFieldValue(resourceInfo, LogAudit::properties);
            if (methodAnnotatedProperties.isPresent()) {
                com.kumuluz.ee.logs.audit.annotations.AuditProperty[] annotatedAuditProperties = methodAnnotatedProperties.get();
                auditProperties.addAll(AuditLogUtil.getAuditProperties(annotatedAuditProperties));
            }

            auditLog.log(name, objectType, auditAction, objectIdentifier, auditProperties.stream().toArray(com.kumuluz.ee.logs.audit.types.AuditProperty[]::new));

            auditLog.flush();
        } catch (Exception e) {
            LOGGER.error("Error while processing AuditLogFilter.", e);
        }
    }

    protected static String getUriCreatedIdentifier(ContainerResponseContext containerResponseContext) {
        URI location = containerResponseContext.getLocation();

        if (location != null && location.getPath() != null) {
            String[] pathShards = location.getPath().split("/");
            if (pathShards.length > 0) {
                return pathShards[pathShards.length - 1];
            }
        }

        return null;
    }

    protected static String getResourceObjectIdentifier(Method resourceMethod, UriInfo uriInfo) {
        Parameter[] parameters = resourceMethod.getParameters();

        //matching object identifier from path - best effort
        String idParamName = null;
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                PathParam annotation = parameters[i].getAnnotation(PathParam.class);
                String annotationVal = annotation == null ? null : annotation.value();
                if ("id".equalsIgnoreCase(annotationVal)) {
                    idParamName = annotationVal;
                    //full match found
                    break;
                } else if (annotationVal != null && annotationVal.toLowerCase().endsWith("id")) {
                    //partial match found
                    idParamName = annotationVal;
                }
            }
        }

        if (idParamName == null) {
            return null;
        }

        return uriInfo.getPathParameters().getFirst(idParamName);
    }

    protected static String getResourceObjectType(Class<?> resourceClass) {

        return resourceClass.getAnnotation(Path.class).value().replaceFirst("/", "");
    }

    protected static String getAuditAction(Method resourceMethod) {

        if (resourceMethod.getAnnotation(GET.class) != null) {
            return DataAuditAction.READ.name();
        } else if (resourceMethod.getAnnotation(POST.class) != null) {
            return DataAuditAction.CREATE.name();
        } else if (resourceMethod.getAnnotation(PUT.class) != null) {
            return DataAuditAction.UPDATE.name();
        } else if (resourceMethod.getAnnotation(PATCH.class) != null) {
            return DataAuditAction.UPDATE.name();
        } else if (resourceMethod.getAnnotation(DELETE.class) != null) {
            return DataAuditAction.DELETE.name();
        }

        return null;
    }
}
