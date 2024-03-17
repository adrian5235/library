package com.adrian.library.copy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.adrian.library.edition.Edition;
import com.adrian.library.borrowing.Borrowing;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Table(name = "copies")
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String signature;

    private LocalDate purchaseDate;

    private double price;

    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "editionId")
    private Edition edition;

    @JsonIgnore
    @OneToMany(mappedBy = "copy")
    private List<Borrowing> borrowings = new ArrayList<>();

    public Copy(String signature, LocalDate purchaseDate, double price, Edition edition) {
        this.signature = signature;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.edition = edition;
    }
}
