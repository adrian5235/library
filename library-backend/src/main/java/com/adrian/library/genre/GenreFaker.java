package com.adrian.library.genre;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreFaker {

    @Autowired
    GenreRepository genreRepository;
    private final Faker faker = new Faker();

    public void saveGenres(int n) {
        List<Genre> genres = genreRepository.findAll();

        for (int i = 0; i < n; i++) {
            String name = faker.book().genre();
            while (contains(genres, name)) {
                name = faker.book().genre();
            }
            Genre genre = new Genre(name);
            genres.add(genre);
            genreRepository.save(genre);
        }
    }

    // checks if genre with a given name exists in given list of genres
    private boolean contains(List<Genre> genres, String name) {
        for (Genre genre : genres) {
            if (genre.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
