package com.adrian.library.book;

import com.adrian.library.author.Author;
import com.adrian.library.author.AuthorRepository;
import com.adrian.library.genre.Genre;
import com.adrian.library.genre.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book get(Integer id) {
        return bookRepository.getReferenceById(id);
    }

    @Override
    public Book create(Book book) {
        List<Author> authors = book.getAuthors();
        List<Genre> genres = book.getGenres();

        for (int i = 0; i < authors.size(); i++) {
            int id = authors.get(i).getId();
            if (id != 0) {
                authors.set(i, authorRepository.getReferenceById(id));
            }
        }
        book.setAuthors(authors);

        for (int i = 0; i < genres.size(); i++) {
            int id = genres.get(i).getId();
            if (id != 0) {
                genres.set(i, genreRepository.getReferenceById(id));
            }
        }
        book.setGenres(genres);

        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public ResponseEntity<String> delete(Integer id) throws IOException {

        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>("The book couldn't be deleted", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>("The book has been deleted", HttpStatus.OK);
    }
}
