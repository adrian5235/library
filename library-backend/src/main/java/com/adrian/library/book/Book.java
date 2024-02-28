package com.adrian.library.book;

import com.adrian.library.author.Author;
import com.adrian.library.edition.Edition;
import com.adrian.library.language.Language;
import com.adrian.library.genre.Genre;
import com.adrian.library.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String title;
    private String titleOriginal;
    private String description;

    @ManyToOne
    @JoinColumn(name = "languageId")
    private Language language;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "bookId"),
            inverseJoinColumns = @JoinColumn(name = "authorId")
    )
    private List<Author> authors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "bookId"),
            inverseJoinColumns = @JoinColumn(name = "genreId")
    )
    private List<Genre> genres = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<Edition> editions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<Reservation> reservations = new ArrayList<>();

    public Book(String title, String titleOriginal, Language language, String description, List<Author> authors,
                List<Genre> genres) {
        this.title = title;
        this.titleOriginal = titleOriginal;
        this.language = language;
        this.description = description;
        this.authors = authors;
        this.genres = genres;
    }

    public Book(int id, String title, String titleOriginal, Language language, String description,
                List<Author> authors, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.titleOriginal = titleOriginal;
        this.language = language;
        this.description = description;
        this.authors = authors;
        this.genres = genres;
    }
}
