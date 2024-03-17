package com.adrian.library.statistics;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularGenres {

    private int position;
    @Id
    private String name;
    private long numberOfBorrowedCopies;
}
