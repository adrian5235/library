package com.adrian.library.borrowing;

import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.user.UserLacksActionPointsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/borrowings")
@PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
public class BorrowingController {

    private final BorrowingServiceImpl service;
    private final BorrowingMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('borrowing:read')")
    List<BorrowingDTO> getAll() {
        return service.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@userServiceImpl.hasId(#userId) or hasAuthority('borrowing:read')")
    List<BorrowingDTO> getByUserId(@PathVariable @P("userId") Integer userId) {
        return service.getByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('borrowing:read')")
    BorrowingDTO get(@PathVariable Integer id) {
        return mapper.toDto(service.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('borrowing:create')")
    Borrowing create(@RequestParam Integer editionId, @RequestParam(required = false) Integer userId)
            throws BookAlreadyBorrowedException, BookAlreadyReservedException, UserLacksActionPointsException {
        return service.create(editionId, userId, null);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('borrowing:update')")
    Borrowing update(@RequestBody BorrowingDTO borrowingDto) {
        return service.update(mapper.toEntity(borrowingDto));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('borrowing:update')")
    void activate(@PathVariable Integer id) {
        service.activate(id);
    }

    @PutMapping("/{id}/finalize")
    @PreAuthorize("hasAuthority('borrowing:update')")
    void finish(@PathVariable Integer id) throws BookAlreadyBorrowedException, BookAlreadyReservedException,
            UserLacksActionPointsException {
        service.finalize(id);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('borrowing:cancel')")
    void cancel(@PathVariable Integer id) throws BookAlreadyReservedException, BookAlreadyBorrowedException,
            UserLacksActionPointsException {
        service.cancel(id);
    }

    @PutMapping("/{id}/chargePaid")
    @PreAuthorize("hasAuthority('borrowing:update')")
    void setChargeAsPaid(@PathVariable Integer id) {
        service.setChargeAsPaid(id);
    }
}
