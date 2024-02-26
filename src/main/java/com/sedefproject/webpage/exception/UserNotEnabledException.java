package com.sedefproject.webpage.exception;

public class UserNotEnabledException extends RuntimeException {

    public UserNotEnabledException() {
        super();
    }

    public UserNotEnabledException(String message) {
        super(message);
    }

    public UserNotEnabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotEnabledException(Throwable cause) {
        super(cause);
    }
}
