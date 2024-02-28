package com.adrian.library.reservation;

import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.loan.LoanServiceImpl;
import com.adrian.library.user.UserLacksActionPointsException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface ReservationService {
    void setLoanService(LoanServiceImpl loanService);
    List<Reservation> getAll();
    List<Reservation> getByUserId(Integer userId);
    Reservation get(Integer id);
    Reservation create(Integer userId, Integer bookId, Integer editionId, LocalDate waitDeadline)
            throws BookAlreadyReservedException, BookAlreadyLoanedException, UserLacksActionPointsException;
    void loan() throws BookAlreadyLoanedException, BookAlreadyReservedException, UserLacksActionPointsException;
    Reservation update(Reservation reservation);
    Reservation cancel(Integer id);
    void cancelReservations();
}
