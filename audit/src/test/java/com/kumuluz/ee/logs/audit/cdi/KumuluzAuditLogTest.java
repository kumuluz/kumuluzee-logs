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

import com.kumuluz.ee.configuration.utils.ConfigurationImpl;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.LogUtil;
import com.kumuluz.ee.logs.audit.cdi.interceptors.LogAuditInterceptor;
import com.kumuluz.ee.logs.audit.loggers.AuditLogger;
import com.kumuluz.ee.logs.audit.test.beans.AuditedBean;
import com.kumuluz.ee.logs.audit.test.loggers.TestLogger;
import com.kumuluz.ee.logs.audit.types.AuditProperty;
import com.kumuluz.ee.logs.messages.LogMessage;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static com.kumuluz.ee.logs.audit.cdi.AuditLog.CONFIG_AUDIT_LOG_DISABLE;

/**
 * @author Gregor Porocnik
 */
public class KumuluzAuditLogTest {

    //mocking production environment
    @Rule
    public WeldInitiator weld = WeldInitiator.from(AuditLog.class, AuditedBean.class, LogAuditInterceptor.class)
            .activate(RequestScoped.class, ApplicationScoped.class).build();


    @BeforeClass
    public static void init() {
        ConfigurationUtil.initialize(new ConfigurationImpl());
    }

    @After
    public void after() {
        //reset messages state after each test
        TestLogger.setMessages(new LinkedList<>());
        System.setProperty(CONFIG_AUDIT_LOG_DISABLE, "false");
    }

    @Test
    public void shouldLogAuditWithApi() {

        LogUtil.getInstance().getLogConfigurator().setLevel(AuditLogger.class.getName(), Level.INFO.toString());

        AuditLog auditLog = weld.select(AuditLog.class).get();

        AuditProperty actionProperty = new AuditProperty("action", "update");
        AuditProperty objectTypeProperty = new AuditProperty("auditObjectType", "user");

        auditLog.log("actionName", actionProperty, objectTypeProperty);

        auditLog.flush();

        List<LogMessage> messages = TestLogger.getMessages();

        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.size());

        LogMessage logMessage = messages.get(0);
        Assert.assertNotNull(logMessage);
        Assert.assertEquals("actionName", logMessage.getMessage());
        Map<String, String> fields = logMessage.getFields();
        Assert.assertEquals("update", fields.get("action"));
        Assert.assertEquals("user", fields.get("auditObjectType"));
    }

    @Test
    public void shouldLogAuditWithAnnotations() {

        LogUtil.getInstance().getLogConfigurator().setLevel(AuditLogger.class.getName(), Level.INFO.toString());

        AuditLog auditLog = weld.select(AuditLog.class).get();

        AuditedBean auditedBean = weld.select(AuditedBean.class).get();

        UUID param1 = UUID.randomUUID();
        String param2 = "TestParam2";

        auditedBean.updateObject(param1, param2);
        auditLog.flush();

        List<LogMessage> messages = TestLogger.getMessages();

        Assert.assertNotNull(messages);
        Assert.assertEquals(1, messages.size());

        LogMessage logMessage = messages.get(0);
        Assert.assertNotNull(logMessage);
        Assert.assertEquals("updateObject", logMessage.getMessage()); //method name
        Map<String, String> fields = logMessage.getFields();
        //class properties
        Assert.assertEquals("classPropertyVal1", fields.get("classPropertyName1"));
        Assert.assertEquals("classPropertyVal2", fields.get("classPropertyName2"));
        //method properties
        Assert.assertEquals("methodPropertyVal1", fields.get("methodPropertyName1"));
        Assert.assertEquals("methodPropertyVal2", fields.get("methodPropertyName2"));
        //method parameters
        Assert.assertEquals(param1.toString(), fields.get("methodParam1"));
        Assert.assertEquals("TestParam2", fields.get("methodParam2"));
    }

}
