package com.adrian.library.author;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorFaker {

    @Autowired
    private AuthorRepository authorRepository;
    private final Faker faker = new Faker();

    public void saveAuthors(int n) {
        List<Author> authors = authorRepository.findAll();

        for (int i = 0; i < n; i++) {
            String name = faker.name().name();
            while (contains(authors, name)) {
                name = faker.name().name();
            }
            Author author = new Author(name);
            authors.add(author);
            authorRepository.save(author);
        }
    }

    // checks if author with a given name exists in given list of authors
    private boolean contains(List<Author> authors, String name) {
        for (Author author : authors) {
            if (author.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
