package com.adrian.library.user;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFaker {

    @Autowired
    UserRepository userRepository;
    private final Faker faker = new Faker();

    public void saveUsers(int n) {
        for (int i = 0; i < n; i++) {
            String name = faker.name().name();
            String email = faker.internet().emailAddress();
            String password = "$2a$10$4m2nPywBnG.AtXNMGIErzeOTiPh1yhdrqXP4acMajcwv5zltC0Mhe";
            Role role = Role.READER;

            User user = new User(name, email, password, role);
            userRepository.save(user);
        }
    }
}
