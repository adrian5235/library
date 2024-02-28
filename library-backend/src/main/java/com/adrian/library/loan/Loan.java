package com.adrian.library.loan;

import com.adrian.library.copy.Copy;
import com.adrian.library.loan.status.LoanStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.adrian.library.reservation.Reservation;
import com.adrian.library.user.User;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate createdOn;

    private LocalDate pickUpDeadline;

    private LocalDate pickedUpOn;

    private LocalDate returnDeadline;

    private LocalDate returnedOn;

    private double charge;

    private double chargePaid;

    private boolean hasChargeBeenPaid;

    private LocalDate chargedOn;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "copyId")
    private Copy copy;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "statusId")
    private LoanStatus status;

    @OneToOne
    @JoinColumn(name = "reservationId", referencedColumnName = "id")
    private Reservation reservation;

    public Loan(LocalDate createdOn, LocalDate pickUpDeadline, LocalDate pickedUpOn, LocalDate returnDeadline,
                LocalDate returnedOn, Copy copy, User user, LoanStatus status) {
        this.createdOn = createdOn;
        this.pickUpDeadline = pickUpDeadline;
        this.pickedUpOn = pickedUpOn;
        this.returnDeadline = returnDeadline;
        this.returnedOn = returnedOn;
        this.copy = copy;
        this.user = user;
        this.status = status;
    }

    public void charge(double chargeAmount, int multiplier) {
        charge += chargeAmount * multiplier;
    }
}
