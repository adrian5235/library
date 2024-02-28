package com.adrian.library.book;

import com.adrian.library.author.Author;
import com.adrian.library.language.Language;
import com.adrian.library.genre.Genre;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class BookDTO {

    private final int id;
    private final String title;
    private final String titleOriginal;
    private final Language language;
    private final String description;
    private final List<Author> authors;
    private final List<Genre> genres;
    private final boolean anyEditionAvailable;
}
