package com.adrian.library.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.adrian.library.user.Permission.*;

@RequiredArgsConstructor
public enum Role {

    USER(
            Set.of()
    ),
    READER(
            Set.of(
                    AUTHOR_READ,
                    GENRE_READ,
                    USER_LOANS_READ,
                    LOAN_CREATE,
                    LOAN_CANCEL,
                    USER_RESERVATIONS_READ,
                    RESERVATION_CREATE,
                    RESERVATION_CANCEL
            )
    ),
    LIBRARIAN(
            Set.of(
                    BOOK_CREATE,
                    BOOK_UPDATE,
                    BOOK_DELETE,
                    AUTHOR_CREATE,
                    AUTHOR_UPDATE,
                    AUTHOR_DELETE,
                    EDITION_READ,
                    EDITION_CREATE,
                    EDITION_UPDATE,
                    EDITION_DELETE,
                    COPY_READ,
                    COPY_CREATE,
                    COPY_UPDATE,
                    GENRE_CREATE,
                    GENRE_UPDATE,
                    GENRE_DELETE,
                    LOAN_READ,
                    LOAN_UPDATE,
                    RESERVATION_READ,
                    RESERVATION_UPDATE,
                    USER_READ,
                    USER_GRANT_USER_ROLE,
                    USER_GRANT_READER_ROLE,
                    STORAGE_UPLOAD,
                    STORAGE_DOWNLOAD
            ),
            READER.getPermissions()
    ),
    // admin as a role for the "main" librarian/s
    ADMIN(
            Set.of(
                    USER_UPDATE,
                    USER_GRANT_LIBRARIAN_ROLE
            ),
            LIBRARIAN.getPermissions()
    );

    Role(Set<Permission> ownPermissions, Set<Permission> inheritedPermissions) {
        Set<Permission> permissions = new HashSet<>();

        permissions.addAll(ownPermissions);
        permissions.addAll(inheritedPermissions);

        this.permissions = permissions;
    }

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
