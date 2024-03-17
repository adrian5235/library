package com.adrian.library.task;

import com.adrian.library.borrowing.BookAlreadyBorrowedException;
import com.adrian.library.borrowing.BorrowingServiceImpl;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.reservation.ReservationServiceImpl;
import com.adrian.library.user.UserLacksActionPointsException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final BorrowingServiceImpl borrowingService;
    private final ReservationServiceImpl reservationService;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void chargeBorrowings() {
        borrowingService.chargeBorrowings();
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void cancelBorrowings() throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyBorrowedException {
        borrowingService.cancelBorrowings();
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void cancelReservations() {
        reservationService.cancelReservations();
    }
}
