/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs.markers;

/**
 * Kumuluz-logs logger interface
 *
 * @author Rok Povse, Marko Skrjanec
 */
public enum CommonsMarker implements Marker {
    ENTRY("ENTRY"), EXIT("EXIT"), RESOURCE_START("RESOURCE_START"), RESOURCE_END("RESOURCE_END");

    private String marker;

    private CommonsMarker(String marker) {
        this.marker = marker;
    }

    public String toString() {
        return marker;
    }
}
