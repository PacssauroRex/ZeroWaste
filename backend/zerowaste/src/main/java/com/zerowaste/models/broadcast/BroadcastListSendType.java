package com.zerowaste.models.broadcast;

public enum BroadcastListSendType {
    MANUAL("MANUAL"),
    INTERVAL("INTERVAL");

    private final String value;

    BroadcastListSendType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
