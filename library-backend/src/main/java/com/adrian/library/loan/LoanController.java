package com.adrian.library.loan;

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
@RequestMapping("/loans")
@PreAuthorize("hasAnyRole('READER', 'LIBRARIAN', 'ADMIN')")
public class LoanController {

    private final LoanServiceImpl service;
    private final LoanMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('loan:read')")
    List<LoanDTO> getAll() {
        return service.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@userServiceImpl.hasId(#userId) or hasAuthority('loan:read')")
    List<LoanDTO> getByUserId(@PathVariable @P("userId") Integer userId) {
        return service.getByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('loan:read')")
    LoanDTO get(@PathVariable Integer id) {
        return mapper.toDto(service.get(id));
    }

    @PostMapping
    Loan create(@RequestParam Integer userId, @RequestParam Integer editionId)
            throws BookAlreadyLoanedException, BookAlreadyReservedException, UserLacksActionPointsException {
        return service.create(userId, editionId, null);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('loan:update')")
    Loan update(@RequestBody LoanDTO loanDto) {
        return service.update(mapper.toEntity(loanDto));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('loan:update')")
    void activate(@PathVariable Integer id) {
        service.activate(id);
    }

    @PutMapping("/{id}/finalize")
    @PreAuthorize("hasAuthority('loan:update')")
    void finish(@PathVariable Integer id) throws BookAlreadyLoanedException, BookAlreadyReservedException,
            UserLacksActionPointsException {
        service.finalize(id);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('loan:cancel')")
    void cancel(@PathVariable Integer id) throws BookAlreadyReservedException, BookAlreadyLoanedException,
            UserLacksActionPointsException {
        service.cancel(id);
    }

    @PutMapping("/{id}/chargePaid")
    @PreAuthorize("hasAuthority('loan:update')")
    void setChargeAsPaid(@PathVariable Integer id) {
        service.setChargeAsPaid(id);
    }
}
