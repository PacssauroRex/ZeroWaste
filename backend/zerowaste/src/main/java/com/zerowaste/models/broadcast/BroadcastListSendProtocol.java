package com.zerowaste.models.broadcast;

public enum BroadcastListSendProtocol {
    EMAIL("EMAIL");

    private final String protocol;

    BroadcastListSendProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }
}
