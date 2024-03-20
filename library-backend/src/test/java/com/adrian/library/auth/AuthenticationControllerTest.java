package com.adrian.library.auth;

import com.google.gson.Gson;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final Gson gson = new Gson();

    @Container
    private static final MySQLContainer container = new MySQLContainer("mysql:latest");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    @Order(1)
    void shouldRegister() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("user")
                .lastName("localhost")
                .email("user@localhost")
                .password("12345qQ!")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void shouldAuthenticate() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("user@localhost")
                .password("12345qQ!")
                .build();

        mockMvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @WithUserDetails("user@localhost")
    void shouldChangePassword() throws Exception {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPassword("12345qQ!")
                .newPassword("12345wW@")
                .confirmationPassword("12345wW@")
                .build();

        mockMvc.perform(patch("/auth/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk());
    }
}
