package com.adrian.library.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    @Override
    public List<Author> getAll() {
        return repository.findAll();
    }

    @Override
    public Author create(Author author) {
        return repository.save(author);
    }

    @Override
    public Author get(Integer id) {
        return repository.getReferenceById(id);
    }

    @Override
    public Author update(Author author) {
        return repository.save(author);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Author...authors) {
        for(Author author: authors) {
            repository.deleteById(author.getId());
        }
    }
}
