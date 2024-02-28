package com.adrian.library.book;

import com.adrian.library.author.Author;
import com.adrian.library.edition.Edition;
import com.adrian.library.language.Language;
import com.adrian.library.genre.Genre;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public BookDTO toDto(Book book) {
        int id = book.getId();
        String title = book.getTitle();
        String titleOriginal = book.getTitleOriginal();
        Language language = book.getLanguage();
        String description = book.getDescription();
        List<Author> authors = book.getAuthors();
        List<Genre> genres = book.getGenres();

        boolean anyEditionAvailable = false;
        for (Edition edition : book.getEditions()) {
            if (edition.getQuantity() > 0) {
                anyEditionAvailable = true;
                break;
            }
        }

        return new BookDTO(id, title, titleOriginal, language, description, authors,
                genres, anyEditionAvailable);
    }

    public Book toEntity(BookDTO bookDTO) {
        int id = bookDTO.getId();
        String title = bookDTO.getTitle();
        String titleOriginal = bookDTO.getTitleOriginal();
        Language language = bookDTO.getLanguage();
        String description = bookDTO.getDescription();
        List<Author> authors = bookDTO.getAuthors();
        List<Genre> genres = bookDTO.getGenres();

        return new Book(id, title, titleOriginal, language, description, authors, genres);
    }
}
