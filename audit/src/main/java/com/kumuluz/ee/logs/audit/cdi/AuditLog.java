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

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.audit.loggers.AuditLogger;
import com.kumuluz.ee.logs.audit.loggers.KumuluzAuditLogger;
import com.kumuluz.ee.logs.audit.loggers.NoOpAuditLogger;
import com.kumuluz.ee.logs.audit.types.AuditProperty;
import com.kumuluz.ee.logs.audit.types.DataAuditAction;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author Gregor Porocnik
 */
@RequestScoped
public class AuditLog {

    private static final Logger LOG = LogManager.getLogger(AuditLog.class.getName());

    protected static final String CONFIG_AUDIT_LOG_DISABLE = "kumuluzee.logs.audit.disable";
    protected static final String CONFIG_AUDIT_LOG_LOGGER_CLASS = "kumuluzee.logs.audit.class";

    private AuditLogger auditLogger;

    public AuditLog() {
    }

    @PostConstruct
    public void init() {

        if (auditLogger != null) {
            return;
        }

        boolean isDisabled = ConfigurationUtil.getInstance().getBoolean(CONFIG_AUDIT_LOG_DISABLE).orElse(false);

        if (isDisabled) {
            LOG.debug("Audit logger is disabled");
            this.auditLogger = new NoOpAuditLogger();
            return;
        }

        final Optional<String> auditLoggerClass = ConfigurationUtil.getInstance().get(CONFIG_AUDIT_LOG_LOGGER_CLASS);

        if (!auditLoggerClass.isPresent()) {
            this.auditLogger = new KumuluzAuditLogger();
            return;
        }

        try {
            final Class<?> clazz = Class.forName(auditLoggerClass.get());
            if (AuditLogger.class.isAssignableFrom(clazz)) {
                this.auditLogger = (AuditLogger) clazz.getDeclaredConstructor().newInstance();
                return;
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            LOG.warn("Unable to create instance of provided class {}", auditLoggerClass.get());
        }

        this.auditLogger = new NoOpAuditLogger();
    }

    public void log(final String actionName, final DataAuditAction dataAuditAction, final Object objectId, final AuditProperty... properties) {
        auditLogger.log(actionName, dataAuditAction, objectId, properties);
    }

    public void log(String actionName, final AuditProperty... properties) {
        auditLogger.log(actionName, properties);
    }

    public void flush() {
        auditLogger.flush();
    }

    /**
     * Common property gets applied to all audit log lines
     *
     * @param property auditProperty
     */
    public void addCommonProperty(AuditProperty property) {
        auditLogger.addCommonProperty(property);
    }
}
