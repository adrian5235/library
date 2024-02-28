package com.adrian.library.statistics;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {
    List<MostPopularBooks> getMostPopularBooks(LocalDate dateFrom, LocalDate dateTo);
    List<MostPopularAuthors> getMostPopularAuthors(LocalDate dateFrom, LocalDate dateTo);
    List<MostPopularGenres> getMostPopularGenres(LocalDate dateFrom, LocalDate dateTo);
}
