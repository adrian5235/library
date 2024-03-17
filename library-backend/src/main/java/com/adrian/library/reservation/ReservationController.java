package com.adrian.library.reservation;

import com.adrian.library.borrowing.BookAlreadyBorrowedException;
import com.adrian.library.user.UserLacksActionPointsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/reservations")
@PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
public class ReservationController {

    private final ReservationServiceImpl service;
    private final ReservationMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('reservation:read')")
    List<ReservationDTO> getAll() {
        return service.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@userServiceImpl.hasId(#userId) or hasAuthority('reservation:read')")
    List<ReservationDTO> getByUserId(@PathVariable @P("userId") Integer userId) {
        return service.getByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('reservation:read')")
    ReservationDTO get(@PathVariable Integer id) {
        return mapper.toDto(service.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('reservation:create')")
    Reservation create(@RequestParam Integer bookId,
                       @RequestParam(required = false) Integer editionId,
                       @RequestParam(required = false) LocalDate waitDeadline)
            throws BookAlreadyReservedException, BookAlreadyBorrowedException, UserLacksActionPointsException {
        return service.create(bookId, editionId, waitDeadline);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('reservation:update')")
    Reservation update(@RequestBody ReservationDTO reservationDTO) {
        return service.update(mapper.toEntity(reservationDTO));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('reservation:cancel')")
    Reservation cancel(@PathVariable Integer id) {
        return service.cancel(id);
    }
}
