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
import com.kumuluz.ee.logs.audit.loggers.AuditLogger;
import com.kumuluz.ee.logs.audit.test.loggers.TestAuditLogger;
import com.kumuluz.ee.logs.audit.test.loggers.TestLogger;
import com.kumuluz.ee.logs.audit.types.AuditProperty;
import com.kumuluz.ee.logs.messages.LogMessage;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import static com.kumuluz.ee.logs.audit.cdi.AuditLog.CONFIG_AUDIT_LOG_DISABLE;
import static com.kumuluz.ee.logs.audit.cdi.AuditLog.CONFIG_AUDIT_LOG_LOGGER_CLASS;

/**
 * @author Gregor Porocnik
 */
public class AuditLogTest {

    //mocking production environment
    @Rule
    public WeldInitiator weld = WeldInitiator.from(AuditLog.class)
            .activate(RequestScoped.class, ApplicationScoped.class).build();


    @BeforeClass
    public static void init() {
        try {
            ConfigurationUtil.initialize(new ConfigurationImpl());
        } catch (IllegalStateException e) {

        }
    }

    @After
    public void after() {
        //reset messages state after each test
        TestLogger.setMessages(new LinkedList<>());
        System.setProperty(CONFIG_AUDIT_LOG_DISABLE, "false");
    }

    @Test
    public void disabledAuditShouldNotLog() {

        System.setProperty(CONFIG_AUDIT_LOG_DISABLE, "true");

        LogUtil.getInstance().getLogConfigurator().setLevel(AuditLogger.class.getName(), Level.INFO.toString());

        AuditLog auditLog = weld.select(AuditLog.class).get();

        AuditProperty actionProperty = new AuditProperty("action", "update");
        AuditProperty objectTypeProperty = new AuditProperty("auditObjectType", "user");

        auditLog.log("actionName", actionProperty, objectTypeProperty);

        auditLog.flush();

        List<LogMessage> messages = TestLogger.getMessages();

        Assert.assertNotNull(messages);
        Assert.assertEquals(0, messages.size());
    }

    @Test
    public void shouldLogAuditWithCustomAuditLogger() {

        TestAuditLogger.logCount = 0;

        System.setProperty(CONFIG_AUDIT_LOG_LOGGER_CLASS, TestAuditLogger.class.getName());

        LogUtil.getInstance().getLogConfigurator().setLevel(AuditLogger.class.getName(), Level.INFO.toString());

        AuditLog auditLog = weld.select(AuditLog.class).get();

        auditLog.log("actionName");

        Assert.assertEquals(1, TestAuditLogger.logCount);
    }

}
