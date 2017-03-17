/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import com.kumuluz.ee.logs.enums.LogLevel;

/**
 * Created by Rok on 14. 03. 2017.
 *
 * @Author Rok Povse, Marko Skrjanec
 */
public interface LogConfigurator {

    void init();
    void setLevel(String logName, LogLevel logLevel);
    LogLevel getLevel(String logName);

}
