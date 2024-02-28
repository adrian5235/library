package com.adrian.library.loan;

import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.reservation.Reservation;
import com.adrian.library.user.UserLacksActionPointsException;

import java.util.List;

public interface LoanService {
    void init();
    List<Loan> getAll();
    List<Loan> getByUserId(Integer id);
    Loan get(Integer id);
    Loan create(Integer userId, Integer editionId, Reservation reservation)
            throws BookAlreadyLoanedException, BookAlreadyReservedException, UserLacksActionPointsException;
    Loan update(Loan loan);
    void finalize(Integer id) throws BookAlreadyLoanedException, BookAlreadyReservedException, UserLacksActionPointsException;
    void activate(Integer id);
    void setChargeAsPaid(Integer id);
    void cancel(Integer id) throws BookAlreadyReservedException, BookAlreadyLoanedException, UserLacksActionPointsException;
    void cancelLoans() throws UserLacksActionPointsException, BookAlreadyReservedException, BookAlreadyLoanedException;
    void chargeLoans();
}
