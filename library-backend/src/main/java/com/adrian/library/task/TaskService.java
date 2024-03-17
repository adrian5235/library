package com.adrian.library.task;

import com.adrian.library.borrowing.BookAlreadyBorrowedException;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.user.UserLacksActionPointsException;

public interface TaskService {
    void chargeBorrowings();
    void cancelBorrowings() throws UserLacksActionPointsException, BookAlreadyReservedException, BookAlreadyBorrowedException;
    void cancelReservations();
}
