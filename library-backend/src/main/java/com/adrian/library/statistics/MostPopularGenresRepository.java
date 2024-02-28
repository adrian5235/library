package com.adrian.library.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MostPopularGenresRepository extends JpaRepository<MostPopularGenres, Integer> {

    @Query(value = "CALL most_popular_genres(?1, ?2)", nativeQuery = true)
    List<MostPopularGenres> findAllByGivenTimePeriod(LocalDate dateFrom, LocalDate dateTo);
}