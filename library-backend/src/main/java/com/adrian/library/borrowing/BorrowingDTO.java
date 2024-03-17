package com.adrian.library.borrowing;

import com.adrian.library.copy.Copy;
import com.adrian.library.borrowing.status.BorrowingStatus;
import com.adrian.library.reservation.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class BorrowingDTO {

    private final int id;
    private final LocalDate createdOn;
    private final LocalDate pickUpDeadline;
    private final LocalDate pickedUpOn;
    private final LocalDate returnDeadline;
    private final LocalDate returnedOn;
    private final double charge;
    private final double chargePaid;
    private final boolean hasChargeBeenPaid;
    private final LocalDate chargedOn;
    private final String notes;
    private final Copy copy;
    private final int userId;
    private final BorrowingStatus status;
    private final Reservation reservation;
}
