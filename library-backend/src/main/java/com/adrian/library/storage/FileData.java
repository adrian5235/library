package com.adrian.library.storage;

import com.adrian.library.edition.Edition;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String type;
    private String filePath;

    @OneToOne
    @JoinColumn(name = "editionId", referencedColumnName = "id")
    private Edition edition;
}
