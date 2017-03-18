package com.kumuluz.ee.logs.types;

import com.kumuluz.ee.logs.enums.LogLevel;
import com.kumuluz.ee.logs.messages.MethodCallLogMessage;
import com.kumuluz.ee.logs.messages.MethodCallExitLogMessage;

/**
 * @author Tilen Faganel
 */
public class LogMethodContext {

    private LogLevel level;

    private Boolean callEnabled;
    private Boolean metricsEnabled;

    private MethodCallLogMessage callMessage;

    private MethodCallExitLogMessage callExitMessage;

    public LogMethodContext(LogMethodMessage entryMessage, LogLevel level) {
        this.callEnabled = entryMessage.isCallEnabled();
        this.metricsEnabled = entryMessage.isMetricsEnabled();

        this.callMessage = entryMessage.getCallMessage();

        this.level = level;
    }

    public Boolean isCallEnabled() {
        return callEnabled;
    }

    public Boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public MethodCallLogMessage getCallMessage() {
        return callMessage;
    }

    public MethodCallExitLogMessage getCallExitMessage() {
        return callExitMessage;
    }

    public void setCallExitMessage(MethodCallExitLogMessage callExitMessage) {
        this.callExitMessage = callExitMessage;
    }

    public LogLevel getLevel() {
        return level;
    }
}
