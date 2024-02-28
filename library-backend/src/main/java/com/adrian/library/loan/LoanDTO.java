package com.adrian.library.loan;

import com.adrian.library.copy.Copy;
import com.adrian.library.loan.status.LoanStatus;
import com.adrian.library.reservation.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class LoanDTO {

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
    private final LoanStatus status;
    private final Reservation reservation;
}
