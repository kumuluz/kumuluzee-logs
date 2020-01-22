package com.kumuluz.ee.logs.audit.test.loggers;

import com.kumuluz.ee.logs.LogConfigurator;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author Gregor Porocnik
 */
public class TestLogConfigurator implements LogConfigurator {

    @Override
    public void setLevel(String logName, String logLevel) {
    }

    @Override
    public String getLevel(String logName) {
        return "INFO";
    }

    @Override
    public void configure() {

    }

    @Override
    public void configure(String config) {

    }

    @Override
    public void configure(Path config) {

    }

    @Override
    public void configure(InputStream config) {

    }
}
