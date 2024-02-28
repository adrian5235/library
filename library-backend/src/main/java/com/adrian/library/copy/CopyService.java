package com.adrian.library.copy;

import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.user.UserLacksActionPointsException;

import java.util.List;

public interface CopyService {
    List<Copy> getAll();
    List<Copy> getByEditionId(Integer editionId);
    Copy get(Integer id);
    Copy create(Copy copy) throws UserLacksActionPointsException, BookAlreadyReservedException, BookAlreadyLoanedException;
    Copy update(Copy copy);
    void delete(Integer id);
}
