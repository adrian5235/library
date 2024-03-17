package com.adrian.library.config;

import com.adrian.library.author.AuthorFaker;
import com.adrian.library.book.BookFaker;
import com.adrian.library.borrowing.BorrowingFaker;
import com.adrian.library.copy.CopyFaker;
import com.adrian.library.edition.EditionFaker;
import com.adrian.library.genre.GenreFaker;
import com.adrian.library.publisher.PublisherFaker;
import com.adrian.library.translator.TranslatorFaker;
import com.adrian.library.user.UserFaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class Faker implements CommandLineRunner {

    private final UserFaker userFaker;
    private final AuthorFaker authorFaker;
    private final GenreFaker genreFaker;
    private final BookFaker bookFaker;
    private final PublisherFaker publisherFaker;
    private final TranslatorFaker translatorFaker;
    private final EditionFaker editionFaker;
    private final CopyFaker copyFaker;
    private final BorrowingFaker borrowingFaker;
    private final Logger logger = LoggerFactory.getLogger(Faker.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("Executing faker");
        Instant start = Instant.now();

        // uncomment to fill the database with random data upon starting application
//        userFaker.saveUsers(10);
//        authorFaker.saveAuthors(20);
//        genreFaker.saveGenres(20);
//        bookFaker.saveBooks(50);
//        publisherFaker.savePublishers(5);
//        translatorFaker.saveTranslators(5);
//        editionFaker.saveEditions(200);
//        copyFaker.saveCopies(300);
//        borrowingFaker.saveBorrowings(200);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        logger.info("Executed faker in " + timeElapsed.toMillis() + " milliseconds");
    }
}
