package com.adrian.library.author;

import java.util.List;

public interface AuthorService {
    List<Author> getAll();
    Author get(Integer id);
    Author create(Author author);
    Author update(Author author);
    void delete(Integer id);
    void delete(Author...authors);
}
