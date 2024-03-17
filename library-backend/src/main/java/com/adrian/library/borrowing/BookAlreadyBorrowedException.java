package com.adrian.library.borrowing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookAlreadyBorrowedException extends Exception {

    public static final String message = "This book has been already borrowed by this user";

    public BookAlreadyBorrowedException() {
        super(message);
    }
}
