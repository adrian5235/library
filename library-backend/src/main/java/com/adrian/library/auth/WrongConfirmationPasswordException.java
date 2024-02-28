package com.adrian.library.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class WrongConfirmationPasswordException extends Exception {

    public static final String message = "Wrong confirmation password";

    public WrongConfirmationPasswordException() {
        super(message);
    }
}
