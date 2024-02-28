package com.adrian.library.miscellaneous;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "miscellaneous")
public class Miscellaneous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String about;
    private String rules;
    private String contact;
}