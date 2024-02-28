package com.adrian.library.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class WrongCurrentPasswordException extends Exception {

    public static final String message = "Wrong current password";

    public WrongCurrentPasswordException() {
        super(message);
    }
}
