package com.adrian.library.reservation;

import com.adrian.library.book.BookRepository;
import com.adrian.library.borrowing.BookAlreadyBorrowedException;
import com.adrian.library.borrowing.Borrowing;
import com.adrian.library.borrowing.BorrowingServiceImpl;
import com.adrian.library.edition.Edition;
import com.adrian.library.edition.EditionRepository;
import com.adrian.library.reservation.status.ReservationStatusRepository;
import com.adrian.library.user.User;
import com.adrian.library.user.UserLacksActionPointsException;
import com.adrian.library.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final BookRepository bookRepository;
    private final EditionRepository editionRepository;
    private BorrowingServiceImpl borrowingService;
    private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Override
    public void setBorrowingService(BorrowingServiceImpl borrowingService) {
        this.borrowingService = borrowingService;
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getByUserId(Integer userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public Reservation get(Integer id) {
        return reservationRepository.getReferenceById(id);
    }

    @Override
    public Reservation create(Integer bookId, Integer editionId, LocalDate waitDeadline)
            throws BookAlreadyReservedException, BookAlreadyBorrowedException, UserLacksActionPointsException {
        Reservation reservation = new Reservation();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName).get();
        List<Reservation> reservations = user.getReservations();
        List<Borrowing> borrowings = user.getBorrowings();

        if (user.getActionPoints() <= 0) {
            throw new UserLacksActionPointsException();
        }

        // if user has already reserved this book, throw exception
        for (Reservation userReservation : reservations) {
            if (userReservation.getStatus().getName().equals("active")) {
                if (userReservation.getBook().getId() == bookId) {
                    throw new BookAlreadyReservedException();
                }
            }
        }

        // if user has already borrowed a copy of this book, throw exception
        for (Borrowing userBorrowing : borrowings) {
            String status = userBorrowing.getStatus().getName();
            if (status.equals("active") || status.equals("awaiting")) {
                if (userBorrowing.getCopy().getEdition().getBook().getId() == bookId) {
                    throw new BookAlreadyBorrowedException();
                }
            }
        }

        reservation.setCreatedOn(LocalDate.now());
        reservation.setUser(user);
        // set borrowing status to active
        reservation.setStatus(reservationStatusRepository.getReferenceById(1));
        reservation.setBook(bookRepository.getReferenceById(bookId));

        if (editionId != null) {
            reservation.setEdition(editionRepository.getReferenceById(editionId));
        }
        // due to lack of the PrimeVue calendar zone configuration
        if (waitDeadline != null) {
            reservation.setWaitDeadline(waitDeadline.plusDays(1));
        }

        user.decreaseActionPoints();

        return reservationRepository.save(reservation);
    }

    // create borrowing out of the reservation if a copy is available
    @Override
    public void borrow() throws BookAlreadyBorrowedException, BookAlreadyReservedException, UserLacksActionPointsException {
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation reservation : reservations) {
            if (reservation.getStatus().getName().equals("active")) {
                Edition edition = reservation.getEdition();
                if (edition != null) {
                    if (edition.getQuantity() > 0) {
                        reservation.setStatus(reservationStatusRepository.getReferenceById(2));
                        reservationRepository.save(reservation);
                        borrowingService.create(edition.getId(), null, reservation);
                    }
                } else {
                    List<Edition> editions = reservation.getBook().getEditions();
                    for (Edition edition1 : editions) {
                        if (edition1.getQuantity() > 0) {
                            reservation.setStatus(reservationStatusRepository.getReferenceById(2));
                            reservationRepository.save(reservation);
                            borrowingService.create(edition1.getId(), null, reservation);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Reservation update(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation cancel(Integer id) {
        Reservation reservation = reservationRepository.getReferenceById(id);
        User user = reservation.getUser();

        reservation.setStatus(reservationStatusRepository.getReferenceById(3));

        user.increaseActionPoints();

        return reservationRepository.save(reservation);
    }

    @Override
    public void cancelReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if (reservation.getStatus().getName().equals("active")) {
                LocalDate waitDeadline =  reservation.getWaitDeadline();
                if (waitDeadline != null) {
                    if (LocalDate.now().isAfter(waitDeadline)) {
                        cancel(reservation.getId());
                        logger.info("Cancelled reservation " + reservation.getId());
                    }
                }
            }
        }
    }
}
