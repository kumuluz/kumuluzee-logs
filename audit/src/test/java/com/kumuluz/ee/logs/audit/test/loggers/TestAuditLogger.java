package com.kumuluz.ee.logs.audit.test.loggers;

import com.kumuluz.ee.logs.audit.loggers.AuditLogger;
import com.kumuluz.ee.logs.audit.types.AuditProperty;
import com.kumuluz.ee.logs.audit.types.DataAuditAction;

public class TestAuditLogger implements AuditLogger {

    public static int logCount = 0;

    @Override
    public void addCommonProperty(AuditProperty property) {
        logCount++;
    }

    @Override
    public void log(String actionName, DataAuditAction dataAuditAction, Object objectId, AuditProperty... properties) {
        logCount++;
    }

    @Override
    public void log(String actionName, AuditProperty... properties) {
        logCount++;
    }

    @Override
    public void flush() {
        logCount++;
    }
}
