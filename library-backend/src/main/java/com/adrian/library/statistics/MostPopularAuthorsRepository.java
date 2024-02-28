package com.adrian.library.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MostPopularAuthorsRepository extends JpaRepository<MostPopularAuthors, Integer> {

    @Query(value = "CALL most_popular_authors(?1, ?2)", nativeQuery = true)
    List<MostPopularAuthors> findAllByGivenTimePeriod(LocalDate dateFrom, LocalDate dateTo);
}