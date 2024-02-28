package com.adrian.library.translator;

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
@Table(name = "translators")
public class Translator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "translator")
    private List<Edition> editions = new ArrayList<>();

    public Translator(String name) {
        this.name = name;
    }
}