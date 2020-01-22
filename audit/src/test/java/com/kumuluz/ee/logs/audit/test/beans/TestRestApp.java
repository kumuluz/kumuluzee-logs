package com.kumuluz.ee.logs.audit.test.beans;

import com.kumuluz.ee.logs.audit.filters.AuditLogFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("")
public class TestRestApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TestAuditedResource.class);
        classes.add(AuditLogFilter.class);

        return classes;
    }
}
