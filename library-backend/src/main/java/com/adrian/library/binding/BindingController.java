package com.adrian.library.binding;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/bindings")
@PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
public class BindingController {

    private final BindingServiceImpl service;

    @GetMapping
    List<Binding> getAll() {
        return service.getAll();
    }

    @PostMapping
    Binding create(@RequestBody Binding binding) {
        return service.create(binding);
    }

    @PutMapping
    Binding update(@RequestBody Binding binding) {
        return service.update(binding);
    }
}

