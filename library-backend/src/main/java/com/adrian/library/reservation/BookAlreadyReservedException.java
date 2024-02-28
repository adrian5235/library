package com.adrian.library.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookAlreadyReservedException extends Exception {

    public static final String message = "This book has been already reserved by this user";

    public BookAlreadyReservedException() {
        super(message);
    }
}
