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
import java.util.UUID;

/**
 * @author Gregor Porocnik
 */
@LogAudit(properties = {@AuditProperty(property = "classPropertyName1", val = "classPropertyVal1"), @AuditProperty(property = "classPropertyName2", val = "classPropertyVal2")})
@ApplicationScoped
public class AuditedBean {

    @LogAudit(properties = {@AuditProperty(property = "methodPropertyName1", val = "methodPropertyVal1"), @AuditProperty(property = "methodPropertyName2", val = "methodPropertyVal2")})
    public void updateObject(@AuditObjectParam("methodParam1") final UUID objectId, @AuditObjectParam("methodParam2") final String object) {
        //nothing to do here..
    }

}
