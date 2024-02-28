package com.adrian.library.author;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorServiceImpl service;

    @GetMapping
    List<Author> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('author:read')")
    Author get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('author:create')")
    Author create(@RequestBody Author author) {
        return service.create(author);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('author:update')")
    Author update(@RequestBody Author author) {
        return service.update(author);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('author:delete')")
    void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
