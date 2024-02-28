package com.adrian.library.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MostPopularBooksRepository extends JpaRepository<MostPopularBooks, Integer> {

    @Query(value = "CALL most_popular_books(?1, ?2)", nativeQuery = true)
    List<MostPopularBooks> findAllByGivenTimePeriod(LocalDate dateFrom, LocalDate dateTo);
}
