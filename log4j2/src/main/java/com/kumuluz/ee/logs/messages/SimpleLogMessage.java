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
public class SimpleLogMessage implements LogMessage {

    private String message;

    private Map<String, String> fields;

    @Override
    public Map<String, String> getFields() {
        return fields;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
