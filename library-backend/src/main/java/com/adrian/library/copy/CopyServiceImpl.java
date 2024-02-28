package com.adrian.library.copy;

import com.adrian.library.edition.Edition;
import com.adrian.library.edition.EditionRepository;
import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.reservation.ReservationServiceImpl;
import com.adrian.library.user.UserLacksActionPointsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CopyServiceImpl implements CopyService {

    private final CopyRepository copyRepository;
    private final EditionRepository editionRepository;
    private final ReservationServiceImpl reservationService;

    @Override
    public List<Copy> getAll() {
        return copyRepository.findAll();
    }

    @Override
    public List<Copy> getByEditionId(Integer editionId) {
        return copyRepository.findByEditionId(editionId);
    }

    @Override
    public Copy get(Integer id) {
        return copyRepository.getReferenceById(id);
    }

    @Override
    public Copy create(Copy copy) throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyLoanedException {
        Edition edition = copy.getEdition();

        if (copy.isAvailable()) {
            edition.increaseQuantity();
            editionRepository.save(edition);
        }

        copy.setPurchaseDate(copy.getPurchaseDate().plusDays(1));

        Copy savedCopy = copyRepository.save(copy);
        reservationService.loan();
        return savedCopy;
    }

    @Override
    public Copy update(Copy copy) {
        Copy copyAboutToGetUpdated = copyRepository.getReferenceById(copy.getId());

        // check if copy availability will change on update
        // if it will, change edition quantity
        if (copy.isAvailable() != copyAboutToGetUpdated.isAvailable()) {
            Edition edition = copy.getEdition();
            if (copy.isAvailable()) {
                edition.increaseQuantity();
            } else {
                edition.decreaseQuantity();
            }
            editionRepository.save(edition);
        }

        copy.setPurchaseDate(copy.getPurchaseDate().plusDays(1));
        return copyRepository.save(copy);
    }

    @Override
    public void delete(Integer id) {
        copyRepository.deleteById(id);
    }
}
