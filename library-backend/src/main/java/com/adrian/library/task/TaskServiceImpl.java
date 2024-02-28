package com.adrian.library.task;

import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.loan.LoanServiceImpl;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.reservation.ReservationServiceImpl;
import com.adrian.library.user.UserLacksActionPointsException;
import com.adrian.library.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final LoanServiceImpl loanService;
    private final ReservationServiceImpl reservationService;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void chargeLoans() {
        loanService.chargeLoans();
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void cancelLoans() throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyLoanedException {
        loanService.cancelLoans();
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void cancelReservations() {
        reservationService.cancelReservations();
    }
}
