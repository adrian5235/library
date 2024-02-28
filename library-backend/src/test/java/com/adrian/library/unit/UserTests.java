package com.adrian.library.unit;

import com.adrian.library.auth.ChangePasswordRequest;
import com.google.gson.Gson;

import org.junit.jupiter.api.MethodOrderer;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTests {

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
    @WithUserDetails("reader@localhost")
    void should_change_password() throws Exception {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPassword("1234")
                .newPassword("123")
                .confirmationPassword("123")
                .build();

        mockMvc.perform(patch("/users/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk());
    }

    // checks if a reader@localhost can access the reader@localhost's data
    @Test
    @WithUserDetails("reader@localhost")
    void shouldGrantAccessById() throws Exception {
        mockMvc.perform(get("/users/1")).andExpect(status().isOk());
    }

    // checks if a reader@localhost can access the librarian@localhost's data
    @Test
    @WithUserDetails("reader@localhost")
    void shouldDenyAccess() throws Exception {
        mockMvc.perform(get("/users/2")).andExpect(status().isForbidden());
    }

    // checks if a librarian@localhost can access the reader@localhost's data
    @Test
    @WithUserDetails("librarian@localhost")
    void shouldGrantAccessByRole() throws Exception {
        mockMvc.perform(get("/users/1")).andExpect(status().isOk());
    }
}
