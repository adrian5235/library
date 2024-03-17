package com.adrian.library.book;

import com.adrian.library.author.Author;
import com.adrian.library.author.AuthorRepository;
import com.adrian.library.genre.Genre;
import com.adrian.library.genre.GenreRepository;
import com.adrian.library.language.Language;
import com.adrian.library.language.LanguageRepository;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class BookFaker {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final Faker faker = new Faker();
    private final Random r = new Random();
    private int m;

    @Transactional
    public void saveBooks(int n) {
        List<Book> books = bookRepository.findAll();

        for (int i = 0; i < n; i ++) {
            Language randomLanguage = randomLanguage();
            String randomTitle;
            String randomTitleOriginal = null;
            String randomDescription = faker.lorem().characters(400, 800,
                    true, false);

            randomTitle = faker.book().title();
            while (contains(books, randomTitle)) {
                randomTitle = faker.book().title();
            }

            // if language is english, there is 33% chance title is not original so book has been translated
            if (randomLanguage.getName().equals("english")) {
                if (r.nextInt(1, 3) == 1) {
                    randomTitleOriginal = faker.book().title();
                    while (randomTitleOriginal.equals(randomTitle)) {
                        randomTitleOriginal = faker.book().title();
                    }
                }
            }

            books.add(bookRepository.save(new Book(
                    randomTitle,
                    randomTitleOriginal,
                    randomLanguage,
                    randomDescription,
                    randomAuthors(),
                    randomGenres()
            )));
        }
    }

    // checks if book with a given title exists in given list of books
    private boolean contains(List<Book> books, String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    private List<Author> randomAuthors() {
        List<Author> authors = authorRepository.findAll();
        List<Author> bookAuthors = new ArrayList<>();
        m = r.nextInt(2)+1;

        for (int i = 0; i < m; i++) {
            bookAuthors.add(authorRepository.getReferenceById(authors.remove(r.nextInt(authors.size())).getId()));
        }
        return bookAuthors;
    }

    @Transactional
    private List<Genre> randomGenres() {
        List<Genre> genres = genreRepository.findAll();
        List<Genre> bookGenres = new ArrayList<>();
        m = r.nextInt(2)+1;

        for (int i = 0; i < m; i++) {
            bookGenres.add(genreRepository.getReferenceById(genres.remove(r.nextInt(genres.size())).getId()));
        }
        return bookGenres;
    }

    private Language randomLanguage() {
        List<Language> languages = languageRepository.findAll();
        return languages.get(r.nextInt(languages.size()));
    }
}
