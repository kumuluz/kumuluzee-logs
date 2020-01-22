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
package com.kumuluz.ee.logs.audit.test.beans;

import com.kumuluz.ee.logs.audit.annotations.AuditObjectParam;
import com.kumuluz.ee.logs.audit.annotations.AuditProperty;
import com.kumuluz.ee.logs.audit.annotations.LogAudit;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.UUID;

/**
 * @author Gregor Porocnik
 */
@Path("/resourceName")
@LogAudit(properties = {@AuditProperty(property = "resPropName1", val = "resPropVal1"), @AuditProperty(property = "resPropName2", val = "resPropVal2")})
@ApplicationScoped
public class TestAuditedResource {

    @Context
    private UriInfo uriInfo;

    @PUT
    @Path("/{id}")
    @LogAudit(name = "customAuditName", auditAction = "AUDIT_ACTION", objectType = "Object", properties = {@AuditProperty(property = "methodPropertyName1", val = "methodPropertyVal1"), @AuditProperty(property = "methodPropertyName2", val = "methodPropertyVal2")})
    public Response putMethodWithFullAuditProps(@AuditObjectParam("id") @QueryParam("id") final UUID objectId, @AuditObjectParam("obj") @QueryParam("obj") final String object) {
        //for testing purpose nothing to do here..

        return Response.ok().build();
    }

    @POST
    public Response postMethodWithFullAuditProps(@QueryParam("obj") final Integer reqObject) {
        //for testing purpose nothing to do here..

        return Response.created(uriInfo.getAbsolutePathBuilder().path("locationReturnedIdentifier").build()).build();
    }

    @GET
    @Path("/{parentId}/{id}")
    public Response getMethodWithoutAuditAnnotation(@PathParam("parentId") final String parentId, @PathParam("id") final String objectId, @AuditObjectParam("methodParam2") @QueryParam("obj") final String object) {
        //nothing to do here..
        return Response.ok().build();
    }

}
