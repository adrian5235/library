package com.adrian.library.book;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface BookService {
    List<Book> getAll();
    Book get(Integer id);
    Book create(Book book);
    Book update(Book book);
    ResponseEntity<String> delete(Integer id) throws IOException;
}
