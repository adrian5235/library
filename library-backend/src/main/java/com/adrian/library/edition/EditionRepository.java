package com.adrian.library.edition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EditionRepository extends JpaRepository<Edition, Integer> {
    List<Edition> findByBookId(Integer bookId);

    @Query(value = "select e from Editions e inner join copies c on e.id = c.edition_id where c.id = ?1",
            nativeQuery = true)
    Edition findByCopyId(Integer copyId);
}
