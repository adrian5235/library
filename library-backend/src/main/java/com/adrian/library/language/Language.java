package com.adrian.library.language;

import com.adrian.library.book.Book;
import com.adrian.library.edition.Edition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "languages")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "language")
    private List<Book> books = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "language")
    private List<Edition> editions = new ArrayList<>();

    public Language(String name) {
        this.name = name;
    }
}