package com.adrian.library.loan;

import com.adrian.library.copy.Copy;
import com.adrian.library.loan.status.LoanStatus;
import com.adrian.library.reservation.Reservation;
import com.adrian.library.user.User;
import com.adrian.library.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LoanMapper {

    private final UserRepository userRepository;

    public LoanDTO toDto(Loan loan) {
        int id = loan.getId();
        LocalDate createdOn = loan.getCreatedOn();
        LocalDate pickUpDeadline = loan.getPickUpDeadline();
        LocalDate pickedUpOn = loan.getPickedUpOn();
        LocalDate returnDeadline = loan.getReturnDeadline();
        LocalDate returnedOn = loan.getReturnedOn();
        double charge = loan.getCharge();
        double chargePaid = loan.getChargePaid();
        boolean hasChargeBeenPaid = loan.isHasChargeBeenPaid();
        LocalDate chargedOn = loan.getChargedOn();
        String notes = loan.getNotes();
        Copy copy = loan.getCopy();
        int userId = loan.getUser().getId();
        LoanStatus status = loan.getStatus();
        Reservation reservation = loan.getReservation();

        return new LoanDTO(id, createdOn, pickUpDeadline, pickedUpOn, returnDeadline, returnedOn, charge,
                chargePaid, hasChargeBeenPaid, chargedOn, notes, copy, userId, status, reservation);
    }

    public Loan toEntity(LoanDTO loanDTO) {
        int id = loanDTO.getId();
        LocalDate createdOn = loanDTO.getCreatedOn();
        LocalDate pickUpDeadline = loanDTO.getPickUpDeadline();
        LocalDate pickedUpOn = loanDTO.getPickedUpOn();
        LocalDate returnDeadline = loanDTO.getReturnDeadline();
        LocalDate returnedOn = loanDTO.getReturnedOn();
        double charge = loanDTO.getCharge();
        double chargePaid = loanDTO.getChargePaid();
        boolean hasChargeBeenPaid = loanDTO.isHasChargeBeenPaid();
        LocalDate chargedOn = loanDTO.getChargedOn();
        String notes = loanDTO.getNotes();
        Copy copy = loanDTO.getCopy();
        User user = userRepository.getReferenceById(loanDTO.getUserId());
        LoanStatus status = loanDTO.getStatus();
        Reservation reservation = loanDTO.getReservation();

        return new Loan(id, createdOn, pickUpDeadline, pickedUpOn, returnDeadline, returnedOn, charge,
                chargePaid, hasChargeBeenPaid, chargedOn, notes, copy, user, status, reservation);
    }
}
