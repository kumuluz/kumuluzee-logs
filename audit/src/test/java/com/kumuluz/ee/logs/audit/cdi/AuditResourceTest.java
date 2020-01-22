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
package com.kumuluz.ee.logs.audit.cdi;

import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.audit.test.beans.TestAuditedResource;
import com.kumuluz.ee.logs.audit.test.beans.TestRestApp;
import com.kumuluz.ee.logs.audit.test.loggers.TestLogCommons;
import com.kumuluz.ee.logs.audit.test.loggers.TestLogConfigurator;
import com.kumuluz.ee.logs.audit.test.loggers.TestLogger;
import com.kumuluz.ee.logs.messages.LogMessage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.kumuluz.ee.logs.audit.cdi.AuditLog.CONFIG_AUDIT_LOG_ENABLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Gregor Porocnik
 */
@RunWith(Arquillian.class)
public class AuditResourceTest {


    @Deployment
    public static WebArchive createDeployment() {

        WebArchive jar = ShrinkWrap
                .create(WebArchive.class)
                .addClasses(TestRestApp.class, TestAuditedResource.class, Logger.class, TestLogCommons.class, TestLogger.class, TestLogConfigurator.class)
                .addPackages(true, "com.kumuluz.ee.logs.audit")
                .addAsResource("META-INF/services/com.kumuluz.ee.logs.Logger")
                .addAsResource("META-INF/services/com.kumuluz.ee.logs.LogCommons")
                .addAsResource("META-INF/services/com.kumuluz.ee.logs.LogConfigurator")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        jar.merge(Maven.resolver().resolve("com.kumuluz.ee.logs:kumuluzee-logs-common:1.4.0-audit").withoutTransitivity().asSingle(JavaArchive.class));

        return jar;
    }

    @Before
    public void before() {
        System.setProperty(CONFIG_AUDIT_LOG_ENABLE, "true");
        TestLogger.setMessages(new LinkedList<>());
    }

    @Test
    public void shouldLogClassAnnotatedAuditGetResourceWithParams() throws IOException {
        URL url = new URL("http://localhost:8080/resourceName/parentInputParamIds/inputParamId?obj=1234");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        assertEquals(200, connection.getResponseCode());

        List<LogMessage> messages = TestLogger.getMessages();
        messages.stream().forEach(this::debugMsg);

        assertEquals(1, messages.size());
        LogMessage logMessage = messages.get(0);
        Map<String, String> fields = logMessage.getFields();
        assertEquals(6, fields.size());
        assertEquals("getMethodWithoutAuditAnnotation", logMessage.getMessage());
        assertEquals("READ", fields.get("audit-action"));
        assertEquals("resourceName", fields.get("audit-object-type"));
        assertEquals("inputParamId", fields.get("audit-object-id"));
        assertEquals("1234", fields.get("methodParam2"));
        //assert class anotated properties since method does not have AuditLog annotation
        assertEquals("resPropVal1", fields.get("resPropName1"));
        assertEquals("resPropVal2", fields.get("resPropName2"));
        //resource parameters not annotated with auditObjectParam are not included
        assertNull(fields.get("parentId"));

    }

    @Test
    public void shouldLogClassAnnotatedAuditPostResourceWithParams() throws IOException {
        URL url = new URL("http://localhost:8080/resourceName?obj=4321");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.connect();

        assertEquals(201, connection.getResponseCode());

        List<LogMessage> messages = TestLogger.getMessages();
        messages.stream().forEach(this::debugMsg);

        assertEquals(1, messages.size());
        LogMessage logMessage = messages.get(0);
        Map<String, String> fields = logMessage.getFields();
        assertEquals(5, fields.size());
        assertEquals("postMethodWithFullAuditProps", logMessage.getMessage());
        assertEquals("CREATE", fields.get("audit-action"));
        assertEquals("resourceName", fields.get("audit-object-type"));
        assertEquals("locationReturnedIdentifier", fields.get("audit-object-id"));
        //assert class anotated properties since method does not have AuditLog annotation
        assertEquals("resPropVal1", fields.get("resPropName1"));
        assertEquals("resPropVal2", fields.get("resPropName2"));

    }

    private void debugMsg(LogMessage logMessage) {
        System.out.println(logMessage.getMessage());
        System.out.println(logMessage.getFields() + "\n");
    }

}
