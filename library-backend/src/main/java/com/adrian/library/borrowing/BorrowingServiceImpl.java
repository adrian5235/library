package com.adrian.library.borrowing;

import com.adrian.library.borrowing.status.BorrowingStatusRepository;
import com.adrian.library.config.Constants;
import com.adrian.library.copy.Copy;
import com.adrian.library.edition.Edition;
import com.adrian.library.edition.EditionRepository;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.reservation.Reservation;
import com.adrian.library.reservation.ReservationServiceImpl;
import com.adrian.library.user.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final UserRepository userRepository;
    private final EditionRepository editionRepository;
    private final BorrowingStatusRepository borrowingStatusRepository;
    private final ReservationServiceImpl reservationService;
    private final Logger logger = LoggerFactory.getLogger(BorrowingServiceImpl.class);

    @Override
    @PostConstruct
    public void init() {
        reservationService.setBorrowingService(this);
    }

    @Override
    public List<Borrowing> getAll() {
        return borrowingRepository.findAll();
    }

    @Override
    public List<Borrowing> getByUserId(Integer userId) {
        return borrowingRepository.findByUserId(userId);
    }

    @Override
    public Borrowing get(Integer id) {
        return borrowingRepository.getReferenceById(id);
    }

    @Override
    public Borrowing create(Integer editionId, Integer userId, Reservation reservation)
            throws BookAlreadyBorrowedException, BookAlreadyReservedException, UserLacksActionPointsException {
        Borrowing borrowing = new Borrowing();
        Edition edition = editionRepository.getReferenceById(editionId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user;

        // check if is trying to borrow a book as a reader and if he/she is authorized,
        // otherwise it's just someone borrowing for himself
        if (userId != null && authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("borrowing:createAs"))
        ) {
            user = userRepository.getReferenceById(userId);
        } else {
            user = userRepository.findByEmail(authentication.getName()).get();
        }

        List<Borrowing> borrowings = user.getBorrowings();
        List<Reservation> reservations = user.getReservations();

        if (user.getActionPoints() <= 0) {
            throw new UserLacksActionPointsException();
        }

        // if user has already borrowed this book, throw exception
        for (Borrowing userBorrowing : borrowings) {
            String status = userBorrowing.getStatus().getName();
            if (status.equals("active") || status.equals("awaiting")) {
                if (userBorrowing.getCopy().getEdition().getBook().getId() == edition.getBook().getId()) {
                    throw new BookAlreadyBorrowedException();
                }
            }
        }

        // if user has already reserved this book, throw exception
        for (Reservation userReservation : reservations) {
            if (userReservation.getStatus().getName().equals("active")) {
                if (userReservation.getBook().getId() == edition.getBook().getId()
                        && borrowing.getReservation() == null) {
                    throw new BookAlreadyReservedException();
                }
            }
        }

        // if borrowing has been created out of a reservation
        if (reservation != null) {
            borrowing.setReservation(reservation);
        }

        // assign an available copy to the borrowing
        List<Copy> copies = edition.getCopies();
        for (Copy copy : copies) {
            if (copy.isAvailable()) {
                borrowing.setCopy(copy);
                copy.setAvailable(false);
                break;
            }
        }

        borrowing.setCreatedOn(LocalDate.now());
        borrowing.setPickUpDeadline(LocalDate.now().plusDays(7));
        borrowing.setUser(user);
        // set borrowing status as awaiting
        borrowing.setStatus(borrowingStatusRepository.getReferenceById(1));

        edition.decreaseQuantity();
        user.decreaseActionPoints();

        return borrowingRepository.save(borrowing);
    }

    @Override
    public Borrowing update(Borrowing borrowing) {
        return borrowingRepository.save(borrowing);
    }

    // set borrowing status to finalized and its returnedOn date
    @Override
    public void finalize(Integer id) throws BookAlreadyBorrowedException, BookAlreadyReservedException,
            UserLacksActionPointsException {
        Borrowing borrowing = borrowingRepository.getReferenceById(id);
        Copy copy = borrowing.getCopy();
        Edition edition = copy.getEdition();
        User user = borrowing.getUser();

        borrowing.setStatus(borrowingStatusRepository.getReferenceById(3));
        borrowing.setReturnedOn(LocalDate.now());

        copy.setAvailable(true);
        edition.increaseQuantity();
        user.increaseActionPoints();

        borrowingRepository.save(borrowing);
        reservationService.borrow();
    }

    // set borrowing status to active, its pickedUpOn and returnDeadline dates
    @Override
    public void activate(Integer id) {
        Borrowing borrowing = borrowingRepository.getReferenceById(id);
        borrowing.setStatus(borrowingStatusRepository.getReferenceById(2));
        borrowing.setPickedUpOn(LocalDate.now());
        borrowing.setReturnDeadline(LocalDate.now().plusMonths(1));
        borrowingRepository.save(borrowing);
    }

    @Override
    public void setChargeAsPaid(Integer id) {
        Borrowing borrowing = borrowingRepository.getReferenceById(id);
        borrowing.setChargePaid(borrowing.getCharge());
        borrowing.setHasChargeBeenPaid(true);
        borrowingRepository.save(borrowing);
    }

    @Override
    public void cancel(Integer id) throws BookAlreadyReservedException, BookAlreadyBorrowedException,
            UserLacksActionPointsException {
        Borrowing borrowing = borrowingRepository.getReferenceById(id);
        Copy copy = borrowing.getCopy();
        Edition edition = copy.getEdition();
        User user = borrowing.getUser();

        borrowing.setStatus(borrowingStatusRepository.getReferenceById(4));
        copy.setAvailable(true);
        edition.increaseQuantity();
        user.increaseActionPoints();

        borrowingRepository.save(borrowing);
        reservationService.borrow();
    }

    @Override
    @Transactional
    public void cancelBorrowings() throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyBorrowedException {
        List<Borrowing> borrowings = borrowingRepository.findAll();

        for (Borrowing borrowing : borrowings) {
            if (borrowing.getPickedUpOn() == null && borrowing.getStatus().getName().equals("awaiting")) {
                if (LocalDate.now().isAfter(borrowing.getPickUpDeadline())) {
                    cancel(borrowing.getId());
                    logger.info("Cancelled borrowing " + borrowing.getId());
                }
            }
        }
    }

    @Override
    public void chargeBorrowings() {
        List<Borrowing> borrowings = borrowingRepository.findAll();
        boolean charged = false;

        for (Borrowing borrowing : borrowings) {
            if (borrowing.getStatus().getName().equals("active")) {
                LocalDate returnDeadline = borrowing.getReturnDeadline();
                if (LocalDate.now().isAfter(returnDeadline)) {
                    LocalDate chargedOn = borrowing.getChargedOn();
                    if (chargedOn != null) {
                        long daysAgoCharged = DAYS.between(chargedOn, LocalDate.now());
                        if (daysAgoCharged > 0) {
                            borrowing.charge(Constants.getSubsequentChargeAmount(), (int) daysAgoCharged);
                            charged = true;
                        }
                    } else {
                        long daysAgoExpired = DAYS.between(returnDeadline, LocalDate.now());
                        if (daysAgoExpired > 0) {
                            borrowing.charge(Constants.getFirstChargeAmount(), 1);
                            charged = true;
                            daysAgoExpired -= 1;
                            if (daysAgoExpired > 0) {
                                borrowing.charge(Constants.getSubsequentChargeAmount(), (int) daysAgoExpired);
                            }
                        }
                    }
                    if (charged) {
                        logger.info("Charged borrowing " + borrowing.getId());
                        borrowing.setChargedOn(LocalDate.now());
                        if (borrowing.isHasChargeBeenPaid()) {
                            borrowing.setHasChargeBeenPaid(false);
                        }
                        borrowingRepository.save(borrowing);
                    }
                }
            }
        }
    }
}
