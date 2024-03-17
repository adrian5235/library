package com.adrian.library.copy;

import com.adrian.library.borrowing.Borrowing;
import com.adrian.library.edition.Edition;
import com.adrian.library.edition.EditionDTO;
import com.adrian.library.edition.EditionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CopyMapper {

    private final CopyRepository copyRepository;
    private final EditionMapper editionMapper;

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
        List<Borrowing> borrowings = existingCopy.getBorrowings();

        return new Copy(id, signature, purchaseDate, price, available, edition, borrowings);
    }
}
