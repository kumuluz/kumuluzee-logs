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
import com.kumuluz.ee.logs.audit.loggers.KumuluzAuditLogger;
import com.kumuluz.ee.logs.audit.test.beans.TestAuditedBean;
import com.kumuluz.ee.logs.audit.test.loggers.TestAuditLogger;
import com.kumuluz.ee.logs.audit.test.loggers.TestLogCommons;
import com.kumuluz.ee.logs.audit.test.loggers.TestLogConfigurator;
import com.kumuluz.ee.logs.audit.test.loggers.TestLogger;
import com.kumuluz.ee.logs.audit.types.AuditProperty;
import com.kumuluz.ee.logs.audit.types.DataAuditAction;
import com.kumuluz.ee.logs.messages.LogMessage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.kumuluz.ee.logs.audit.cdi.AuditLog.CONFIG_AUDIT_LOG_ENABLE;
import static com.kumuluz.ee.logs.audit.cdi.AuditLog.CONFIG_AUDIT_LOG_LOGGER_CLASS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gregor Porocnik
 */
@ExtendWith(ArquillianExtension.class)
public class AuditLogTest {

    @Deployment
    public static WebArchive createDeployment() {

        ResourceBundle versionsBundle = ResourceBundle.getBundle("META-INF/kumuluzee/logs/audit/versions");

        WebArchive jar = ShrinkWrap
                .create(WebArchive.class)
                .addClasses(TestAuditedBean.class, Logger.class, TestLogCommons.class, TestLogger.class, TestLogConfigurator.class)
                .addPackages(true, "com.kumuluz.ee.logs.audit")
                .addAsResource("META-INF/services/com.kumuluz.ee.logs.Logger")
                .addAsResource("META-INF/services/com.kumuluz.ee.logs.LogCommons")
                .addAsResource("META-INF/services/com.kumuluz.ee.logs.LogConfigurator")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        jar.merge(Maven.resolver().resolve("com.kumuluz.ee.logs:kumuluzee-logs-common:" + versionsBundle.getString("kumuluzee-logs-common-version")).withoutTransitivity().asSingle(JavaArchive.class));

        return jar;
    }

    @Inject
    private AuditLog auditLog;

    @BeforeAll
    public void before() {
        System.setProperty(CONFIG_AUDIT_LOG_LOGGER_CLASS, KumuluzAuditLogger.class.getName());
        TestLogger.setMessages(new LinkedList<>());
        System.setProperty(CONFIG_AUDIT_LOG_ENABLE, "true");
    }

    @Test
    public void shouldLogAuditWithGenericApi() {

        AuditProperty actionProperty = new AuditProperty(AuditProperty.AUDIT_ACTION_PROPERTY, "exampleAction");
        AuditProperty objectTypeProperty = new AuditProperty(AuditProperty.AUDIT_OBJECT_TYPE, "exampleObjectType");

        auditLog.log("exampleMethodName", actionProperty, objectTypeProperty);
        auditLog.flush();

        List<LogMessage> messages = TestLogger.getMessages();

        assertNotNull(messages);
        assertEquals(1, messages.size());
        LogMessage logMessage = messages.get(0);
        assertEquals("exampleMethodName", logMessage.getMessage());
        Map<String, String> fields = logMessage.getFields();
        assertEquals("exampleAction", fields.get(AuditProperty.AUDIT_ACTION_PROPERTY));
        assertEquals("exampleObjectType", fields.get(AuditProperty.AUDIT_OBJECT_TYPE));
    }

    @Test
    void shouldLogAudit() {

        auditLog.log("exampleMethodName", "exampleObjectType", "exampleAction", "id", new AuditProperty("propName", "propValue"));
        auditLog.flush();

        List<LogMessage> messages = TestLogger.getMessages();

        assertNotNull(messages);
        assertEquals(1, messages.size());
        LogMessage logMessage = messages.get(0);
        assertEquals("exampleMethodName", logMessage.getMessage());
        Map<String, String> fields = logMessage.getFields();
        assertEquals("exampleAction", fields.get(AuditProperty.AUDIT_ACTION_PROPERTY));
        assertEquals("exampleObjectType", fields.get(AuditProperty.AUDIT_OBJECT_TYPE));
    }

    @Test
    void shouldLogNullWithNullParams() {

        auditLog.log(null, null, (DataAuditAction) null, null, null);
        auditLog.flush();

        List<LogMessage> messages = TestLogger.getMessages();

        assertNotNull(messages);
        assertEquals(1, messages.size());
        LogMessage logMessage = messages.get(0);
        assertNull(logMessage.getMessage());
        Map<String, String> fields = logMessage.getFields();
        assertNull(fields);
    }

    @Test
    void disabledAuditShouldNotLog() {

        System.setProperty(CONFIG_AUDIT_LOG_ENABLE, "false");

        AuditProperty actionProperty = new AuditProperty(AuditProperty.AUDIT_ACTION_PROPERTY, "update");
        AuditProperty objectTypeProperty = new AuditProperty(AuditProperty.AUDIT_OBJECT_TYPE, "user");

        auditLog.log("updateProp", actionProperty, objectTypeProperty);
        auditLog.flush();

        List<LogMessage> messages = TestLogger.getMessages();
        assertTrue(messages.isEmpty());
    }

    @Test
    void shouldLogAuditWithCustomAuditLogger() {

        System.setProperty(CONFIG_AUDIT_LOG_LOGGER_CLASS, TestAuditLogger.class.getName());

        auditLog.log("actionName");

        assertEquals(1, TestAuditLogger.logCount);
    }

}
