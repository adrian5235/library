package com.adrian.library.genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();
    Genre get(Integer id);
    Genre create(Genre genre);
    Genre update(Genre genre);
    void delete(Integer id);
}
