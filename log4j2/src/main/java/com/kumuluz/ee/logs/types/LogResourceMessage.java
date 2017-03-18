package com.kumuluz.ee.logs.types;

import com.kumuluz.ee.logs.messages.ResourceInvokeEndLogMessage;

/**
 * @author Tilen Faganel
 */
public class LogResourceMessage {

    private Boolean invokeEnabled;
    private Boolean metricsEnabled;

    private ResourceInvokeEndLogMessage invokeMessage;

    public LogResourceMessage enableInvoke(ResourceInvokeEndLogMessage invokeMessage) {
        this.invokeMessage = invokeMessage;
        this.invokeEnabled = true;

        return this;
    }

    public LogResourceMessage enableMetrics() {
        this.metricsEnabled = true;

        return this;
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
}
