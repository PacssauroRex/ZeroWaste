package com.zerowaste.services.broadcasts.errors;

public class BroadcastListNotFoundException extends RuntimeException {
    public BroadcastListNotFoundException(String message) {
        super(message);
    }
}
