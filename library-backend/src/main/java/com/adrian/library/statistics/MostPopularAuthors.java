package com.adrian.library.statistics;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularAuthors {

    private int position;
    @Id
    private String name;
    private long numberOfLoanedCopies;
}
