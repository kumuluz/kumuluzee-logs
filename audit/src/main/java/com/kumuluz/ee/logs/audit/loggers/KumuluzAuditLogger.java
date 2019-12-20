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
package com.kumuluz.ee.logs.audit.loggers;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.audit.types.AuditProperty;
import com.kumuluz.ee.logs.audit.types.DataAuditAction;
import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.messages.SimpleLogMessage;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Gregor Porocnik
 */
public class KumuluzAuditLogger implements AuditLogger {

    public static final String AUDIT_ACTION_PROPERTY = "auditAction";
    public static final String AUDIT_OBJECT_ID = "auditObjectId";

    private static final Logger LOG = LogManager.getLogger(AuditLogger.class.getName());

    private List<AuditProperty> commonProperties = new LinkedList<>();
    private List<AuditLogLine> auditList = new LinkedList<>();

    public KumuluzAuditLogger() {
    }

    @Override
    public void addCommonProperty(AuditProperty property) {
        commonProperties.add(property);
    }

    @Override
    public void log(final String actionName, final DataAuditAction dataAuditAction, final Object objectId, final AuditProperty... properties) {
        logAudit(actionName, dataAuditAction, objectId, properties);
    }

    @Override
    public void log(String actionName, final AuditProperty... properties) {
        logAudit(actionName, null, null, properties);
    }

    private void logAudit(final String actionName, final DataAuditAction dataAuditAction, final Object objectId, final AuditProperty... properties) {

        String logLevel = LogManager.getLogLevel(AuditLogger.class.getName());
        boolean loggerEnabled = LogLevel.valueOf(logLevel).compareTo(LogLevel.INFO) <= 0;

        if (!loggerEnabled) {
            return;
        }

        final AuditLogLine line = new AuditLogLine(actionName);

        if (null != dataAuditAction) {
            final AuditProperty actionProperty = new AuditProperty(AUDIT_ACTION_PROPERTY, dataAuditAction.name());
            line.add(actionProperty);
        }

        if (null != objectId) {
            final AuditProperty idProperty = new AuditProperty(AUDIT_OBJECT_ID, String.valueOf(objectId));
            line.add(idProperty);
        }

        line.addProperties(properties);
        auditList.add(line);
    }

    @Override
    public void flush() {

        auditList.forEach(audit -> {
            audit.properties.addAll(commonProperties);
            LOG.info(audit.getLogMessage());
        });
        auditList.clear();
    }

    public static class AuditLogLine {

        private String actionName;
        private Set<AuditProperty> properties = new HashSet<>();

        protected AuditLogLine(String actionName) {
            this.actionName = actionName;
        }

        protected Set<AuditProperty> getProperties() {
            return properties;
        }

        protected void add(AuditProperty property) {

            if (null == property) {
                return;
            }

            this.properties.add(property);
        }

        private void addProperties(AuditProperty[] properties) {
            for (AuditProperty property : properties) {
                if (null != property) {
                    this.properties.add(property);
                }
            }
        }

        public SimpleLogMessage getLogMessage() {

            final SimpleLogMessage msg = new SimpleLogMessage();

            msg.setMessage(actionName);
            properties.forEach(property ->
                    msg.addField(property.getProperty(), property.getValue() == null ? null : property.getValue().toString())
            );

            return msg;
        }

    }
}
