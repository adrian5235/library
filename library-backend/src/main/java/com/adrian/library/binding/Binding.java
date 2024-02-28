package com.adrian.library.binding;

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
@Table(name = "bindings")
public class Binding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "binding")
    private List<Edition> editions = new ArrayList<>();

    public Binding(String name) {
        this.name = name;
    }
}