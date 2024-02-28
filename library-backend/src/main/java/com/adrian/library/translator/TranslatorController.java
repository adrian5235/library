package com.adrian.library.translator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/translators")
@PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
public class TranslatorController {

    private final TranslatorServiceImpl service;

    @GetMapping
    List<Translator> getAll() {
        return service.getAll();
    }

    @PostMapping
    Translator create(@RequestBody Translator translator) {
        return service.create(translator);
    }

    @PutMapping
    Translator update(@RequestBody Translator translator) {
        return service.update(translator);
    }
}
