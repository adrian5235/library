package com.adrian.library.loan;

import com.adrian.library.config.Constants;
import com.adrian.library.copy.Copy;
import com.adrian.library.copy.CopyRepository;
import com.adrian.library.edition.Edition;
import com.adrian.library.edition.EditionRepository;
import com.adrian.library.loan.status.LoanStatusRepository;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.reservation.Reservation;
import com.adrian.library.reservation.ReservationServiceImpl;
import com.adrian.library.user.User;
import com.adrian.library.user.UserLacksActionPointsException;
import com.adrian.library.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final EditionRepository editionRepository;
    private final CopyRepository copyRepository;
    private final LoanStatusRepository loanStatusRepository;
    @Autowired private final ReservationServiceImpl reservationService;
    private final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    @Override
    @PostConstruct
    public void init() {
        reservationService.setLoanService(this);
    }

    @Override
    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    @Override
    public List<Loan> getByUserId(Integer userId) {
        return loanRepository.findByUserId(userId);
    }

    @Override
    public Loan get(Integer id) {
        return loanRepository.getReferenceById(id);
    }

    @Override
    public Loan create(Integer userId, Integer editionId, Reservation reservation)
            throws BookAlreadyLoanedException, BookAlreadyReservedException, UserLacksActionPointsException {
        Loan loan = new Loan();
        Edition edition = editionRepository.getReferenceById(editionId);
        User user = userRepository.getReferenceById(userId);
        List<Loan> loans = user.getLoans();
        List<Reservation> reservations = user.getReservations();

        if (user.getActionPoints() <= 0) {
            throw new UserLacksActionPointsException();
        }

        // if user has already loaned this book, throw exception
        for (Loan userLoan : loans) {
            String status = userLoan.getStatus().getName();
            if (status.equals("aktywne") || status.equals("oczekujące")) {
                if (userLoan.getCopy().getEdition().getBook().getId() == edition.getBook().getId()) {
                    throw new BookAlreadyLoanedException();
                }
            }
        }

        // if user has already reserved this book, throw exception
        for (Reservation userReservation : reservations) {
            if (userReservation.getStatus().getName().equals("aktywna")) {
                if (userReservation.getBook().getId() == edition.getBook().getId()
                        && loan.getReservation() == null) {
                    throw new BookAlreadyReservedException();
                }
            }
        }

        // if loan has been created out of a reservation
        if (reservation != null) {
            loan.setReservation(reservation);
        }

        // assign an available copy to the loan
        List<Copy> copies = edition.getCopies();
        for (Copy copy : copies) {
            if (copy.isAvailable()) {
                loan.setCopy(copy);
                copy.setAvailable(false);
                break;
            }
        }

        loan.setCreatedOn(LocalDate.now());
        loan.setPickUpDeadline(LocalDate.now().plusDays(7));
        loan.setUser(userRepository.getReferenceById(userId));
        // set loan status as a pending
        loan.setStatus(loanStatusRepository.getReferenceById(1));

        edition.decreaseQuantity();
        user.decreaseActionPoints();

        return loanRepository.save(loan);
    }

    @Override
    public Loan update(Loan loan) {
        return loanRepository.save(loan);
    }

    // set loan status to finalized and its returnedOn date
    @Override
    public void finalize(Integer id) throws BookAlreadyLoanedException, BookAlreadyReservedException,
            UserLacksActionPointsException {
        Loan loan = loanRepository.getReferenceById(id);
        Copy copy = loan.getCopy();
        Edition edition = copy.getEdition();
        User user = loan.getUser();

        loan.setStatus(loanStatusRepository.getReferenceById(3));
        loan.setReturnedOn(LocalDate.now());

        copy.setAvailable(true);
        edition.increaseQuantity();
        user.increaseActionPoints();

        loanRepository.save(loan);
        reservationService.loan();
    }

    // set loan status to active, its pickedUpOn and returnDeadline dates
    @Override
    public void activate(Integer id) {
        Loan loan = loanRepository.getReferenceById(id);
        loan.setStatus(loanStatusRepository.getReferenceById(2));
        loan.setPickedUpOn(LocalDate.now());
        loan.setReturnDeadline(LocalDate.now().plusMonths(1));
        loanRepository.save(loan);
    }

    @Override
    public void setChargeAsPaid(Integer id) {
        Loan loan = loanRepository.getReferenceById(id);
        loan.setChargePaid(loan.getCharge());
        loan.setHasChargeBeenPaid(true);
        loanRepository.save(loan);
    }

    @Override
    public void cancel(Integer id) throws BookAlreadyReservedException, BookAlreadyLoanedException,
            UserLacksActionPointsException {
        Loan loan = loanRepository.getReferenceById(id);
        Copy copy = loan.getCopy();
        Edition edition = copy.getEdition();
        User user = loan.getUser();

        loan.setStatus(loanStatusRepository.getReferenceById(4));
        copy.setAvailable(true);
        edition.increaseQuantity();
        user.increaseActionPoints();

        loanRepository.save(loan);
        reservationService.loan();
    }

    @Override
    @Transactional
    public void cancelLoans() throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyLoanedException {
        List<Loan> loans = loanRepository.findAll();

        for (Loan loan : loans) {
            if (loan.getPickedUpOn() == null && loan.getStatus().getName().equals("oczekujące")) {
                if (LocalDate.now().isAfter(loan.getPickUpDeadline())) {
                    cancel(loan.getId());
                    logger.info("Cancelled loan " + loan.getId());
                }
            }
        }
    }

    @Override
    public void chargeLoans() {
        List<Loan> loans = loanRepository.findAll();
        boolean charged = false;

        for (Loan loan : loans) {
            if (loan.getStatus().getName().equals("aktywne")) {
                LocalDate returnDeadline = loan.getReturnDeadline();
                if (LocalDate.now().isAfter(returnDeadline)) {
                    LocalDate chargedOn = loan.getChargedOn();
                    if (chargedOn != null) {
                        long daysAgoCharged = DAYS.between(chargedOn, LocalDate.now());
                        if (daysAgoCharged > 0) {
                            loan.charge(Constants.subsequentChargeAmount, (int) daysAgoCharged);
                            charged = true;
                        }
                    } else {
                        long daysAgoExpired = DAYS.between(returnDeadline, LocalDate.now());
                        if (daysAgoExpired > 0) {
                            loan.charge(Constants.firstChargeAmount, 1);
                            charged = true;
                            daysAgoExpired -= 1;
                            if (daysAgoExpired > 0) {
                                loan.charge(Constants.subsequentChargeAmount, (int) daysAgoExpired);
                            }
                        }
                    }
                    if (charged) {
                        logger.info("Charged loan " + loan.getId());
                        loan.setChargedOn(LocalDate.now());
                        if (loan.isHasChargeBeenPaid()) {
                            loan.setHasChargeBeenPaid(false);
                        }
                        loanRepository.save(loan);
                    }
                }
            }
        }
    }
}
