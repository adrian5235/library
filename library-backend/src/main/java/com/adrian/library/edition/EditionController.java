package com.adrian.library.edition;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/editions")
public class EditionController {

    private final EditionServiceImpl service;
    private final EditionMapper mapper;

    @GetMapping
    List<EditionDTO> getAll() {
        return service.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/onePerBook")
    List<EditionDTO> getOnePerBook() {
        return service.getOnePerBook()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/book/{bookId}")
    List<EditionDTO> getByBookId(@PathVariable Integer bookId) {
        return service.getByBookId(bookId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/copy/{copyId}")
    EditionDTO getByCopyId(@PathVariable Integer copyId) {
        return mapper.toDto(service.getByCopyId(copyId));
    }

    @GetMapping("/{id}")
    EditionDTO get(@PathVariable Integer id) {
        return mapper.toDto(service.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('edition:create')")
    Edition create(@RequestBody Edition edition) {
        return service.create(edition);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('edition:update')")
    Edition update(@RequestBody Edition edition) {
        return service.update(edition);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('edition:delete')")
    void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
