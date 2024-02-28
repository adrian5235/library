package com.adrian.library.copy;

import com.adrian.library.edition.Edition;
import com.adrian.library.edition.EditionRepository;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class CopyFaker {

    @Autowired
    CopyRepository copyRepository;
    @Autowired
    EditionRepository editionRepository;
    private final Faker faker = new Faker();
    Random r = new Random();

    @Transactional
    public void saveCopies(int n) {
        for (int i = 0; i < n; i ++) {
            String signature = faker.code().ean13();
            Calendar calendar = Calendar.getInstance();
            Date date = faker.date().past(2100, TimeUnit.DAYS);
            calendar.setTime(date);
            LocalDate purchaseDate =  LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            double price = faker.number().randomDouble(2, 10, 100);
            Edition edition = randomEdition();

            copyRepository.save(new Copy(signature, purchaseDate, price, edition));

            edition.increaseQuantity();
            editionRepository.save(edition);
        }
    }

    private Edition randomEdition() {
        List<Edition> editions = editionRepository.findAll();
        return editions.get(r.nextInt(editions.size()));
    }
}
