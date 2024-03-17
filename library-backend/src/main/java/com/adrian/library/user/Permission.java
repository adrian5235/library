package com.adrian.library.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_GRANT_USER_ROLE("user:grantUserRole"),
    USER_GRANT_READER_ROLE("user:grantReaderRole"),
    USER_GRANT_LIBRARIAN_ROLE("user:grantLibrarianRole"),
    USER_BORROWINGS_READ("user:userBorrowingsRead"),
    USER_RESERVATIONS_READ("user:userReservationsRead"),

    BOOK_CREATE("book:create"),
    BOOK_UPDATE("book:update"),
    BOOK_DELETE("book:delete"),

    AUTHOR_READ("author:read"),
    AUTHOR_CREATE("author:create"),
    AUTHOR_UPDATE("author:update"),
    AUTHOR_DELETE("author:delete"),

    GENRE_READ("genre:read"),
    GENRE_CREATE("genre:create"),
    GENRE_UPDATE("genre:update"),
    GENRE_DELETE("genre:delete"),

    EDITION_READ("edition:read"),
    EDITION_CREATE("edition:create"),
    EDITION_UPDATE("edition:update"),
    EDITION_DELETE("edition:delete"),

    COPY_READ("copy:read"),
    COPY_CREATE("copy:create"),
    COPY_UPDATE("copy:update"),

    BORROWING_READ("borrowing:read"),
    BORROWING_CREATE("borrowing:create"),
    BORROWING_CREATE_AS("borrowing:createAs"),
    BORROWING_UPDATE("borrowing:update"),
    BORROWING_CANCEL("borrowing:cancel"),

    RESERVATION_READ("reservation:read"),
    RESERVATION_CREATE("reservation:create"),
    RESERVATION_UPDATE("reservation:update"),
    RESERVATION_CANCEL("reservation:cancel"),

    STORAGE_UPLOAD("storage:upload"),
    STORAGE_DOWNLOAD("storage:download")

    ;

    @Getter
    private final String permission;
}
