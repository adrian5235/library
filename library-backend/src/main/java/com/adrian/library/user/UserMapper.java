package com.adrian.library.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserRepository repository;

    public UserDTO toDto(User user) {
        int id = user.getId();
        String name = user.getName();
        String email = user.getEmail();
        int actionPoints = user.getActionPoints();
        Role role = user.getRole();

        return new UserDTO(id, name, email, actionPoints, role);
    }

    public User toEntity(UserDTO userDTO) {
        int id = userDTO.getId();
        String name = userDTO.getName();
        String email = userDTO.getEmail();
        String password = repository.getReferenceById(id).getPassword();
        int actionPoints = userDTO.getActionPoints();
        Role role = userDTO.getRole();

        return new User(id, name, email, password, actionPoints, role);
    }
}
