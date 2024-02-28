package com.adrian.library.genre;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.adrian.library.book.Book;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private List<Book> books = new ArrayList<>();

    public Genre(String name) {
        this.name = name;
    }
}
