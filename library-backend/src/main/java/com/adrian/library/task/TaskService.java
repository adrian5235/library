package com.adrian.library.task;

import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.user.UserLacksActionPointsException;

public interface TaskService {
    void chargeLoans();
    void cancelLoans() throws UserLacksActionPointsException, BookAlreadyReservedException, BookAlreadyLoanedException;
    void cancelReservations();
}
