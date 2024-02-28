package com.adrian.library.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/genres")
public class GenreController {

    private final GenreServiceImpl service;

    @GetMapping
    List<Genre> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('genre:read')")
    Genre get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('genre:create')")
    Genre create(@RequestBody Genre genre) {
        return service.create(genre);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('genre:update')")
    Genre update(@RequestBody Genre genre) {
        return service.update(genre);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('genre:delete')")
    void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}

