package com.adrian.library.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDTO {

    private final int id;
    private final String name;
    private final String email;
    private final int actionPoints;
    private final Role role;
}
