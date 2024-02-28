package com.adrian.library.edition;

import com.adrian.library.book.Book;
import com.adrian.library.book.BookRepository;
import com.adrian.library.binding.Binding;
import com.adrian.library.binding.BindingRepository;
import com.adrian.library.language.Language;
import com.adrian.library.language.LanguageRepository;
import com.adrian.library.publisher.Publisher;
import com.adrian.library.publisher.PublisherRepository;
import com.adrian.library.translator.Translator;
import com.adrian.library.translator.TranslatorRepository;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class EditionFaker {

    @Autowired
    EditionRepository editionRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    TranslatorRepository translatorRepository;
    @Autowired
    BindingRepository bindingRepository;
    private final Faker faker = new Faker();
    Random r = new Random();

    @Transactional
    public void saveEditions(int n) {
        for (int i = 0; i < n; i ++) {
            Edition edition = new Edition(
                    randomIsbn(),
                    randomReleaseYear(),
                    randomLanguage(),
                    randomBinding(),
                    randomNumberOfPages(),
                    randomWeight(),
                    randomDimensions(),
                    randomPublisher(),
                    randomBook()
            );

            // if book has been translated, there is 50% chance this edition is a translation
            if (edition.getBook().getTitleOriginal() != null) {
                int j = r.nextInt(1,2);
                if (j == 1) {
                    edition.setTranslator(randomTranslator());
                }
            }
            editionRepository.save(edition);
        }
    }

    private String randomIsbn() {
        return faker.code().isbn13();
    }

    private String randomReleaseYear() {
        return String.valueOf(faker.number().numberBetween(1950, LocalDate.now().getYear()));
    }

    private int randomNumberOfPages() {
        return faker.number().numberBetween(100, 1000);
    }

    private String randomWeight() {
        return r.nextInt(500, 1000) + "g";
    }

    private String randomDimensions() {
        return ((double)((int)(r.nextDouble(10, 15) * 100))) / 100 + " x "
                + ((double)((int)(r.nextDouble(1.5, 4) * 100))) / 100 + " x "
                + ((double)((int)(r.nextDouble(18, 25) * 100))) / 100 + " cm";
    }

    private Publisher randomPublisher() {
        List<Publisher> publishers = publisherRepository.findAll();
        return publishers.get(r.nextInt(publishers.size()));
    }

    private Language randomLanguage() {
        List<Language> languages = languageRepository.findAll();
        return languages.get(r.nextInt(languages.size()));
    }

    private Translator randomTranslator() {
        List<Translator> translators = translatorRepository.findAll();
        return translators.get(r.nextInt(translators.size()));
    }

    private Binding randomBinding() {
        List<Binding> bindings = bindingRepository.findAll();
        return bindings.get(r.nextInt(bindings.size()));
    }

    private Book randomBook() {
        List<Book> books = bookRepository.findAll();
        return books.get(r.nextInt(books.size()));
    }
}
