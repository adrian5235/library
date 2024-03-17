package com.adrian.library.task.scheduler;

import com.adrian.library.borrowing.BookAlreadyBorrowedException;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.user.UserLacksActionPointsException;

public interface TaskSchedulerService {
    void chargeBorrowingsSchedule();
    void cancelBorrowingsSchedule() throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyBorrowedException;
    void cancelReservationsSchedule();
}
