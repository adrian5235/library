package com.adrian.library.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/stats")
@PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN', 'READER')")
public class StatisticsController {

    private final StatisticsServiceImpl service;

    @GetMapping("/mostPopularBooks")
    List<MostPopularBooks> getMostPopularBooks(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateTo
    ) {
        return service.getMostPopularBooks(dateFrom, dateTo);
    }

    @GetMapping("/mostPopularAuthors")
    List<MostPopularAuthors> getMostPopularAuthors(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateTo
    ) {
        return service.getMostPopularAuthors(dateFrom, dateTo);
    }

    @GetMapping("/mostPopularGenres")
    List<MostPopularGenres> getMostPopularGenres(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateTo
    ) {
        return service.getMostPopularGenres(dateFrom, dateTo);
    }
}
