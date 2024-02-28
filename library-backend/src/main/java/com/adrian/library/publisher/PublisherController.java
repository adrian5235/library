package com.adrian.library.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/publishers")
@PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
public class PublisherController {

    private final PublisherServiceImpl service;

    @GetMapping
    List<Publisher> getAll() {
        return service.getAll();
    }

    @PostMapping
    Publisher create(@RequestBody Publisher publisher) {
        return service.create(publisher);
    }

    @PutMapping
    Publisher update(@RequestBody Publisher publisher) {
        return service.update(publisher);
    }
}
