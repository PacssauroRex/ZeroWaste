package com.zerowaste.zerowaste.services.users.exceptions;

public class UserWithSameEmailAlreadyExistsException extends RuntimeException {
    public UserWithSameEmailAlreadyExistsException(String message) {
        super(message);
    }
}
