package com.adrian.library.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserLacksActionPointsException extends Exception {

    public static final String message = "This user is not allowed to borrow/reserve anymore due to lack of the action points";

    public UserLacksActionPointsException() {
        super(message);
    }
}
