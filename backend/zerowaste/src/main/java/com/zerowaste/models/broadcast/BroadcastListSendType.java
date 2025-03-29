package com.zerowaste.models.broadcast;

public enum BroadcastType {
    MANUAL("MANUAL"),
    INTERVAL("INTERVAL");

    private final String value;

    BroadcastType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
