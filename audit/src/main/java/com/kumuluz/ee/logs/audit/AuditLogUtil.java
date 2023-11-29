package com.kumuluz.ee.logs.audit;

/*
 *  Copyright (c) 2014-2020 Kumuluz and/or its affiliates
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

import com.kumuluz.ee.logs.audit.annotations.AuditObjectParam;
import com.kumuluz.ee.logs.audit.annotations.LogAudit;
import com.kumuluz.ee.logs.audit.types.AuditProperty;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.UriInfo;

import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Gregor Porocnik
 */
public class AuditLogUtil {


    public static Set<AuditProperty> getAuditProperties(Parameter[] methodParameters, UriInfo uriInfo) {
        return IntStream.range(0, methodParameters.length)
                .mapToObj(paramIndex -> {
                    AuditObjectParam auditObjectId = methodParameters[paramIndex].getAnnotation(AuditObjectParam.class);

                    if (auditObjectId == null) {
                        return null;
                    }

                    PathParam pathParam = methodParameters[paramIndex].getAnnotation(PathParam.class);
                    if (pathParam != null) {
                        return new com.kumuluz.ee.logs.audit.types.AuditProperty(auditObjectId.value(), uriInfo.getPathParameters().getFirst(pathParam.value()));
                    }

                    QueryParam queryParam = methodParameters[paramIndex].getAnnotation(QueryParam.class);
                    if (queryParam != null) {
                        return new com.kumuluz.ee.logs.audit.types.AuditProperty(auditObjectId.value(), uriInfo.getQueryParameters().getFirst(queryParam.value()));
                    }

                    return null;
                }).collect(Collectors.toSet());
    }

    public static Set<AuditProperty> getAuditProperties(Parameter[] methodParameters, Object[] parameterValues) {
        return IntStream.range(0, methodParameters.length)
                .mapToObj(paramIndex -> {
                    AuditObjectParam auditObjectId = methodParameters[paramIndex].getAnnotation(AuditObjectParam.class);

                    return auditObjectId == null ? null : new com.kumuluz.ee.logs.audit.types.AuditProperty(auditObjectId.value(), parameterValues[paramIndex]);
                }).collect(Collectors.toSet());
    }

    public static Set<AuditProperty> getAuditProperties(com.kumuluz.ee.logs.audit.annotations.AuditProperty[] auditPropertyAnnotations) {

        if (null != auditPropertyAnnotations) {
            return Stream.of(auditPropertyAnnotations).map(prop -> new com.kumuluz.ee.logs.audit.types.AuditProperty(prop.property(), prop.val())).collect(Collectors.toSet());
        }

        return null;
    }

    public static <T> Optional<T> getLogAnnotatedFieldValue(ResourceInfo resourceInfo, Function<LogAudit, T> propertyFetchFunction) {
        //get resource method annotated value
        LogAudit methodLogAuditAnnotation = resourceInfo.getResourceMethod().getAnnotation(LogAudit.class);

        Optional<T> methodAnnotatedValue = getLogAnnotatedFieldValue(methodLogAuditAnnotation, propertyFetchFunction);

        return methodAnnotatedValue.isPresent() ? methodAnnotatedValue : getLogAnnotatedFieldValue(resourceInfo.getResourceClass().getAnnotation(LogAudit.class), propertyFetchFunction);
    }

    public static <T> Optional<T> getLogAnnotatedFieldValue(LogAudit logAudit, Function<LogAudit, T> propertyFetchFunction) {

        if (null == logAudit) {
            return Optional.empty();
        }

        T logAnnotatedPropertyVal = propertyFetchFunction.apply(logAudit);

        if (logAnnotatedPropertyVal != null) {
            if (!(logAnnotatedPropertyVal instanceof String) || !((String) logAnnotatedPropertyVal).isEmpty()) {
                return Optional.of(logAnnotatedPropertyVal);
            }
        }

        return Optional.empty();
    }

}
