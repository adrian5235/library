package com.adrian.library.book;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/books")
public class BookController {

    private final BookServiceImpl service;
    private final BookMapper mapper;

    @GetMapping
    List<BookDTO> getAll() {
        return service.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    BookDTO get(@PathVariable Integer id) {
        return mapper.toDto(service.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('book:create')")
    Book create(@RequestBody Book book) {
        return service.create(book);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('book:update')")
    Book update(@RequestBody BookDTO bookDTO) {
        return service.update(mapper.toEntity(bookDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('book:delete')")
    ResponseEntity<String> delete(@PathVariable Integer id) throws IOException {
        return service.delete(id);
    }
}

