package com.adrian.library.borrowing;

import com.adrian.library.copy.Copy;
import com.adrian.library.borrowing.status.BorrowingStatus;
import com.adrian.library.reservation.Reservation;
import com.adrian.library.user.User;
import com.adrian.library.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BorrowingMapper {

    private final UserRepository userRepository;

    public BorrowingDTO toDto(Borrowing borrowing) {
        int id = borrowing.getId();
        LocalDate createdOn = borrowing.getCreatedOn();
        LocalDate pickUpDeadline = borrowing.getPickUpDeadline();
        LocalDate pickedUpOn = borrowing.getPickedUpOn();
        LocalDate returnDeadline = borrowing.getReturnDeadline();
        LocalDate returnedOn = borrowing.getReturnedOn();
        double charge = borrowing.getCharge();
        double chargePaid = borrowing.getChargePaid();
        boolean hasChargeBeenPaid = borrowing.isHasChargeBeenPaid();
        LocalDate chargedOn = borrowing.getChargedOn();
        String notes = borrowing.getNotes();
        Copy copy = borrowing.getCopy();
        int userId = borrowing.getUser().getId();
        BorrowingStatus status = borrowing.getStatus();
        Reservation reservation = borrowing.getReservation();

        return new BorrowingDTO(id, createdOn, pickUpDeadline, pickedUpOn, returnDeadline, returnedOn, charge,
                chargePaid, hasChargeBeenPaid, chargedOn, notes, copy, userId, status, reservation);
    }

    public Borrowing toEntity(BorrowingDTO borrowingDTO) {
        int id = borrowingDTO.getId();
        LocalDate createdOn = borrowingDTO.getCreatedOn();
        LocalDate pickUpDeadline = borrowingDTO.getPickUpDeadline();
        LocalDate pickedUpOn = borrowingDTO.getPickedUpOn();
        LocalDate returnDeadline = borrowingDTO.getReturnDeadline();
        LocalDate returnedOn = borrowingDTO.getReturnedOn();
        double charge = borrowingDTO.getCharge();
        double chargePaid = borrowingDTO.getChargePaid();
        boolean hasChargeBeenPaid = borrowingDTO.isHasChargeBeenPaid();
        LocalDate chargedOn = borrowingDTO.getChargedOn();
        String notes = borrowingDTO.getNotes();
        Copy copy = borrowingDTO.getCopy();
        User user = userRepository.getReferenceById(borrowingDTO.getUserId());
        BorrowingStatus status = borrowingDTO.getStatus();
        Reservation reservation = borrowingDTO.getReservation();

        return new Borrowing(id, createdOn, pickUpDeadline, pickedUpOn, returnDeadline, returnedOn, charge,
                chargePaid, hasChargeBeenPaid, chargedOn, notes, copy, user, status, reservation);
    }
}
