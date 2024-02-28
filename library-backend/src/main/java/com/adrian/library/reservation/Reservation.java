package com.adrian.library.reservation;

import com.adrian.library.edition.Edition;
import com.adrian.library.loan.Loan;
import com.adrian.library.reservation.status.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.adrian.library.book.Book;
import com.adrian.library.user.User;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate createdOn;

    private LocalDate waitDeadline;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "statusId")
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "editionId")
    private Edition edition;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @JsonIgnore
    @OneToOne(mappedBy = "reservation")
    private Loan loan;
}
