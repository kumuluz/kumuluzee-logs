/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs.messages;

import java.util.Map;

/**
 * Created by Rok on 17. 03. 2017.
 *
 * @Author Rok Povse, Marko Skrjanec
 */
public interface LogMessage {

    Map<String, String> getFields();
    String getMessage();
}
