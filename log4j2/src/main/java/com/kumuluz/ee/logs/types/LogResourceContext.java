package com.kumuluz.ee.logs.types;

import com.kumuluz.ee.logs.messages.ResourceInvokeLogMessage;
import com.kumuluz.ee.logs.messages.ResourceInvokeEndLogMessage;

/**
 * @author Tilen Faganel
 */
public class LogResourceContext {

    private Boolean invokeEnabled;
    private Boolean metricsEnabled;

    private ResourceInvokeEndLogMessage invokeMessage;

    private ResourceInvokeLogMessage invokeEndMessage;

    public LogResourceContext(LogResourceMessage resourceMessage) {
        this.invokeEnabled = resourceMessage.isInvokeEnabled();
        this.metricsEnabled = resourceMessage.isMetricsEnabled();

        this.invokeMessage = resourceMessage.getInvokeMessage();
    }

    public Boolean isInvokeEnabled() {
        return invokeEnabled;
    }

    public Boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    public ResourceInvokeEndLogMessage getInvokeMessage() {
        return invokeMessage;
    }

    public ResourceInvokeLogMessage getInvokeEndMessage() {
        return invokeEndMessage;
    }

    public void setInvokeEndMessage(ResourceInvokeLogMessage invokeEndMessage) {
        this.invokeEndMessage = invokeEndMessage;
    }
}
