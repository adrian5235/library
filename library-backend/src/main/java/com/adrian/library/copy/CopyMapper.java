package com.adrian.library.copy;

import com.adrian.library.edition.Edition;
import com.adrian.library.edition.EditionDTO;
import com.adrian.library.edition.EditionMapper;
import com.adrian.library.loan.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CopyMapper {

    @Autowired
    private CopyRepository copyRepository;
    @Autowired
    private EditionMapper editionMapper;

    public CopyDTO toDto(Copy copy) {
        int id = copy.getId();
        String signature = copy.getSignature();
        LocalDate purchaseDate = copy.getPurchaseDate();
        double price = copy.getPrice();
        boolean available = copy.isAvailable();
        EditionDTO edition = editionMapper.toDto(copy.getEdition());

        return new CopyDTO(id, signature, purchaseDate, price, available, edition);
    }

    public Copy toEntity(CopyDTO copyDTO) {
        int id = copyDTO.getId();
        String signature = copyDTO.getSignature();
        LocalDate purchaseDate = copyDTO.getPurchaseDate();
        double price = copyDTO.getPrice();
        boolean available = copyDTO.isAvailable();
        Edition edition = editionMapper.toEntity(copyDTO.getEdition());

        Copy existingCopy = copyRepository.getReferenceById(copyDTO.getId());
        List<Loan> loans = existingCopy.getLoans();

        return new Copy(id, signature, purchaseDate, price, available, edition, loans);
    }
}
