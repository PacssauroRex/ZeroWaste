package com.zerowaste.zerowaste.application.services.users.exceptions;

public class UserWithSameEmailAlreadyExistsException extends RuntimeException {
    public UserWithSameEmailAlreadyExistsException(String message) {
        super(message);
    }
}
