package com.adrian.library.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/users")
@PreAuthorize("hasAnyRole('USER', 'READER', 'LIBRARIAN', 'ADMIN')")
public class UserController {

    private final UserServiceImpl service;
    private final UserMapper mapper;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    List<UserDTO> getAll() {
        return service.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@userServiceImpl.hasId(#id) or hasAuthority('user:read')")
    UserDTO get(@PathVariable @P("id") Integer id) {
        return mapper.toDto(service.get(id));
    }

    @GetMapping("/readers")
    @PreAuthorize("hasAuthority('user:read')")
    List<UserDTO> getReaders() {
        return service.getReaders()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/grantUserRole")
    @PreAuthorize("hasAuthority('user:grantUserRole')")
    void grantUserRole(@PathVariable Integer id) {
        service.grantUserRole(id);
    }

    @PutMapping("/{id}/grantReaderRole")
    @PreAuthorize("hasAuthority('user:grantReaderRole')")
    void grantReaderRole(@PathVariable Integer id) {
        service.grantReaderRole(id);
    }

    @PutMapping("/{id}/grantLibrarianRole")
    @PreAuthorize("hasAuthority('user:grantLibrarianRole')")
    void grantLibrarianRole(@PathVariable Integer id) {
        service.grantLibrarianRole(id);
    }
}