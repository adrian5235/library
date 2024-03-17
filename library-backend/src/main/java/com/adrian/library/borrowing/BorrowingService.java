package com.adrian.library.borrowing;

import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.reservation.Reservation;
import com.adrian.library.user.UserLacksActionPointsException;

import java.util.List;

public interface BorrowingService {
    void init();
    List<Borrowing> getAll();
    List<Borrowing> getByUserId(Integer id);
    Borrowing get(Integer id);
    Borrowing create(Integer editionId, Integer userId, Reservation reservation)
            throws BookAlreadyBorrowedException, BookAlreadyReservedException, UserLacksActionPointsException;
    Borrowing update(Borrowing borrowing);
    void finalize(Integer id) throws BookAlreadyBorrowedException, BookAlreadyReservedException, UserLacksActionPointsException;
    void activate(Integer id);
    void setChargeAsPaid(Integer id);
    void cancel(Integer id) throws BookAlreadyReservedException, BookAlreadyBorrowedException, UserLacksActionPointsException;
    void cancelBorrowings() throws UserLacksActionPointsException, BookAlreadyReservedException, BookAlreadyBorrowedException;
    void chargeBorrowings();
}
