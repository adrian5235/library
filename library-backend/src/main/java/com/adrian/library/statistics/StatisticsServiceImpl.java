package com.adrian.library.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final MostPopularBooksRepository mostPopularBooksRepository;
    private final MostPopularAuthorsRepository mostPopularAuthorsRepository;
    private final MostPopularGenresRepository mostPopularGenresRepository;

    @Override
    public List<MostPopularBooks> getMostPopularBooks(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom != null && dateTo != null) {
            return mostPopularBooksRepository.findAllByGivenTimePeriod(dateFrom, dateTo);
        }

        return mostPopularBooksRepository.findAll();
    }

    @Override
    public List<MostPopularAuthors> getMostPopularAuthors(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom != null && dateTo != null) {
            return mostPopularAuthorsRepository.findAllByGivenTimePeriod(dateFrom, dateTo);
        }

        return mostPopularAuthorsRepository.findAll();
    }

    @Override
    public List<MostPopularGenres> getMostPopularGenres(LocalDate dateFrom, LocalDate dateTo) {
        if (dateFrom != null && dateTo != null) {
            return mostPopularGenresRepository.findAllByGivenTimePeriod(dateFrom, dateTo);
        }

        return mostPopularGenresRepository.findAll();
    }
}
