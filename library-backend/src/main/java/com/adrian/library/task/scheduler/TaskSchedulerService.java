package com.adrian.library.task.scheduler;

import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.user.UserLacksActionPointsException;

public interface TaskSchedulerService {
    void chargeLoansSchedule();
    void cancelLoansSchedule() throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyLoanedException;
    void cancelReservationsSchedule();
}
