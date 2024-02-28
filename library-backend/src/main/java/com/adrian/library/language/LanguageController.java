package com.adrian.library.language;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/languages")
@PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
public class LanguageController {

    private final LanguageServiceImpl service;

    @GetMapping
    List<Language> getAll() {
        return service.getAll();
    }

    @PostMapping
    Language create(@RequestBody Language language) {
        return service.create(language);
    }

    @PutMapping
    Language update(@RequestBody Language language) {
        return service.update(language);
    }
}

