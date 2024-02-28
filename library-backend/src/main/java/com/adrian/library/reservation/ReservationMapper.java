package com.adrian.library.reservation;

import com.adrian.library.book.Book;
import com.adrian.library.edition.Edition;
import com.adrian.library.loan.Loan;
import com.adrian.library.reservation.status.ReservationStatus;
import com.adrian.library.user.User;
import com.adrian.library.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final UserRepository userRepository;

    public ReservationDTO toDto(Reservation reservation) {
        int id = reservation.getId();
        LocalDate createdOn = reservation.getCreatedOn();
        LocalDate waitDeadline = reservation.getWaitDeadline();
        String notes = reservation.getNotes();
        ReservationStatus status = reservation.getStatus();
        Book book = reservation.getBook();
        Edition edition = reservation.getEdition();
        int userId = reservation.getUser().getId();
        Loan loan = reservation.getLoan();

        return new ReservationDTO(id, createdOn, waitDeadline, notes, status, book, edition, userId, loan);
    }

    public Reservation toEntity(ReservationDTO reservationDTO) {
        int id = reservationDTO.getId();
        LocalDate createdOn = reservationDTO.getCreatedOn();
        LocalDate waitDeadline = reservationDTO.getWaitDeadline();
        String notes = reservationDTO.getNotes();
        ReservationStatus status = reservationDTO.getStatus();
        Book book = reservationDTO.getBook();
        Edition edition = reservationDTO.getEdition();
        User user = userRepository.getReferenceById(reservationDTO.getUserId());
        Loan loan = reservationDTO.getLoan();

        return new Reservation(id, createdOn, waitDeadline, notes, status, book, edition, user, loan);
    }
}
