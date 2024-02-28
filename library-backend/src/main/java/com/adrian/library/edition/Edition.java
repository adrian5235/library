package com.adrian.library.edition;

import com.adrian.library.copy.Copy;
import com.adrian.library.binding.Binding;
import com.adrian.library.language.Language;
import com.adrian.library.publisher.Publisher;
import com.adrian.library.storage.FileData;
import com.adrian.library.translator.Translator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.adrian.library.book.Book;
import com.adrian.library.reservation.Reservation;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
@DynamicUpdate
@Table(name = "editions")
public class Edition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String isbn13;

    private String releaseYear;

    private int numberOfPages;

    private String weight;

    private String dimensions;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "publisherId")
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(name = "languageId")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "translatorId")
    private Translator translator;

    @ManyToOne
    @JoinColumn(name = "bindingId")
    private Binding binding;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;

    @JsonIgnore
    @OneToMany(mappedBy = "edition")
    private List<Copy> copies = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "edition")
    private List<Reservation> reservations = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "edition")
    private FileData image;

    public Edition(String isbn13, String releaseYear, Language language, Binding binding, int numberOfPages,
                   String weight, String dimensions, Publisher publisher, Book book) {
        this.isbn13 = isbn13;
        this.releaseYear = releaseYear;
        this.language = language;
        this.binding = binding;
        this.numberOfPages = numberOfPages;
        this.weight = weight;
        this.dimensions = dimensions;
        this.publisher = publisher;
        this.book = book;
    }

    public Edition(int id, String name, String isbn13, String releaseYear, Language language, Binding binding,
                   int numberOfPages, String weight, String dimensions, int quantity, Publisher publisher,
                   Translator translator, Book book, FileData image) {
        this.id = id;
        this.name = name;
        this.isbn13 = isbn13;
        this.releaseYear = releaseYear;
        this.language = language;
        this.binding = binding;
        this.numberOfPages = numberOfPages;
        this.weight = weight;
        this.dimensions = dimensions;
        this.quantity = quantity;
        this.publisher = publisher;
        this.translator = translator;
        this.book = book;
        this.image = image;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        quantity--;
    }
}
