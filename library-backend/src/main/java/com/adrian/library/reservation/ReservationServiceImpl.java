package com.adrian.library.reservation;

import com.adrian.library.book.BookRepository;
import com.adrian.library.edition.Edition;
import com.adrian.library.edition.EditionRepository;
import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.loan.Loan;
import com.adrian.library.loan.LoanServiceImpl;
import com.adrian.library.reservation.status.ReservationStatusRepository;
import com.adrian.library.user.User;
import com.adrian.library.user.UserLacksActionPointsException;
import com.adrian.library.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private LoanServiceImpl loanService;
    private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Override
    public void setLoanService(LoanServiceImpl loanService) {
        this.loanService = loanService;
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
    public Reservation create(Integer userId, Integer bookId, Integer editionId, LocalDate waitDeadline)
            throws BookAlreadyReservedException, BookAlreadyLoanedException, UserLacksActionPointsException {
        Reservation reservation = new Reservation();
        User user = userRepository.getReferenceById(userId);
        List<Reservation> reservations = user.getReservations();
        List<Loan> loans = user.getLoans();

        if (user.getActionPoints() <= 0) {
            throw new UserLacksActionPointsException();
        }

        // if user has already reserved this book, throw exception
        for (Reservation userReservation : reservations) {
            if (userReservation.getStatus().getName().equals("aktywna")) {
                if (userReservation.getBook().getId() == bookId) {
                    throw new BookAlreadyReservedException();
                }
            }
        }

        // if user has already loaned this book, throw exception
        for (Loan userLoan : loans) {
            String status = userLoan.getStatus().getName();
            if (status.equals("aktywne") || status.equals("oczekujące")) {
                if (userLoan.getCopy().getEdition().getBook().getId() == bookId) {
                    throw new BookAlreadyLoanedException();
                }
            }
        }

        reservation.setCreatedOn(LocalDate.now());
        reservation.setUser(userRepository.getReferenceById(userId));
        // set loan status as an active
        reservation.setStatus(reservationStatusRepository.getReferenceById(1));
        reservation.setBook(bookRepository.getReferenceById(bookId));

        if (editionId != null) {
            reservation.setEdition(editionRepository.getReferenceById(editionId));
        }
        // due to lack of the primevue calendar zone configuration
        if (waitDeadline != null) {
            reservation.setWaitDeadline(waitDeadline.plusDays(1));
        }

        user.decreaseActionPoints();

        return reservationRepository.save(reservation);
    }

    // create loan out of the reservation if a copy is available
    @Override
    public void loan() throws BookAlreadyLoanedException, BookAlreadyReservedException, UserLacksActionPointsException {
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation reservation : reservations) {
            if (reservation.getStatus().getName().equals("aktywna")) {
                Edition edition = reservation.getEdition();
                if (edition != null) {
                    if (edition.getQuantity() > 0) {
                        reservation.setStatus(reservationStatusRepository.getReferenceById(2));
                        reservationRepository.save(reservation);
                        loanService.create(reservation.getUser().getId(), edition.getId(), reservation);
                    }
                } else {
                    List<Edition> editions = reservation.getBook().getEditions();
                    for (Edition edition1 : editions) {
                        if (edition1.getQuantity() > 0) {
                            reservation.setStatus(reservationStatusRepository.getReferenceById(2));
                            reservationRepository.save(reservation);
                            loanService.create(reservation.getUser().getId(), edition1.getId(), reservation);
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
            if (reservation.getStatus().getName().equals("aktywna")) {
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
