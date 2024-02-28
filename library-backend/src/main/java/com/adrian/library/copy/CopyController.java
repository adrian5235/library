package com.adrian.library.copy;

import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.user.UserLacksActionPointsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/copies")
@PreAuthorize("hasRole('LIBRARIAN')")
public class CopyController {

    private final CopyServiceImpl service;
    private final CopyMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('copy:read')")
    List<CopyDTO> getAll() {
        return service.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/edition/{editionId}")
    @PreAuthorize("hasAuthority('copy:read')")
    List<Copy> getByEditionId(@PathVariable Integer editionId) {
        return service.getByEditionId(editionId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('copy:read')")
    Copy get(@PathVariable Integer id) {
        return service.get(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('copy:create')")
    Copy create(@RequestBody Copy copy) throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyLoanedException {
        return service.create(copy);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('copy:update')")
    Copy update(@RequestBody CopyDTO copyDTO) {
        return service.update(mapper.toEntity(copyDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('copy:delete')")
    void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
