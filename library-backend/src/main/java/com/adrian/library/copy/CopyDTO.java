package com.adrian.library.copy;

import com.adrian.library.edition.EditionDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class CopyDTO {

    private final int id;
    private final String signature;
    private final LocalDate purchaseDate;
    private final double price;
    private final boolean available;
    private final EditionDTO edition;
}
