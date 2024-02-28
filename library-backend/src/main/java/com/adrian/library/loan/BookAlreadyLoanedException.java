package com.adrian.library.loan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookAlreadyLoanedException extends Exception {

    public static final String message = "This book has been already loaned by this user";

    public BookAlreadyLoanedException() {
        super(message);
    }
}
