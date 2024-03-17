package com.adrian.library.reservation;

import com.adrian.library.book.Book;
import com.adrian.library.edition.Edition;
import com.adrian.library.borrowing.Borrowing;
import com.adrian.library.reservation.status.ReservationStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class ReservationDTO {

    private final int id;
    private final LocalDate createdOn;
    private final LocalDate waitDeadline;
    private final String notes;
    private final ReservationStatus status;
    private final Book book;
    private final Edition edition;
    private final int userId;
    private final Borrowing borrowing;
}
