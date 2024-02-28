package com.adrian.library.loan.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.adrian.library.loan.Loan;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "loan_statuses")
public class LoanStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<Loan> loans = new ArrayList<>();
}
