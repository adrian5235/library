package com.adrian.library.reservation;

import com.adrian.library.borrowing.BookAlreadyBorrowedException;
import com.adrian.library.borrowing.BorrowingServiceImpl;
import com.adrian.library.user.UserLacksActionPointsException;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    void setBorrowingService(BorrowingServiceImpl borrowingService);
    List<Reservation> getAll();
    List<Reservation> getByUserId(Integer userId);
    Reservation get(Integer id);
    Reservation create(Integer bookId, Integer editionId, LocalDate waitDeadline)
            throws BookAlreadyReservedException, BookAlreadyBorrowedException, UserLacksActionPointsException;
    void borrow() throws BookAlreadyBorrowedException, BookAlreadyReservedException, UserLacksActionPointsException;
    Reservation update(Reservation reservation);
    Reservation cancel(Integer id);
    void cancelReservations();
}
