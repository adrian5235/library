package com.adrian.library.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository repository;

    @Override
    public List<Genre> getAll() {
        return repository.findAll();
    }

    @Override
    public Genre get(Integer id) {
        return repository.getReferenceById(id);
    }

    @Override
    public Genre create(Genre genre) {
        return repository.save(genre);
    }

    @Override
    public Genre update(Genre genre) {
        return repository.save(genre);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
