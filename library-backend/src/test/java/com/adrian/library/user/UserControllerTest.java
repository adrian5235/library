package com.adrian.library.user;

import com.google.gson.Gson;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository repository;
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
    @WithUserDetails("librarian@localhost")
    void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)))
                .andExpect(jsonPath("$[0].email", is("reader@localhost")))
                .andExpect(jsonPath("$[1].email", is("librarian@localhost")))
                .andExpect(jsonPath("$[2].email", is("admin@localhost")));
    }

    @Test
    @WithUserDetails("reader@localhost")
    void shouldBeGivenAccessToTheirOwnDataAndReturnIt() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("reader@localhost")));
    }

    @Test
    @WithUserDetails("reader@localhost")
    void shouldNotBeGivenAccessToOtherUserData() throws Exception {
        mockMvc.perform(get("/users/2")).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("librarian@localhost")
    void shouldBeGivenAccessToOtherUserData() throws Exception {
        mockMvc.perform(get("/users/1")).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin@localhost")
    void shouldGrantRoles() throws Exception {
        User reader = repository.findById(2).get();
        assertThat(reader.getRole().name()).isEqualTo("LIBRARIAN");

        mockMvc.perform(put("/users/2/grantUserRole")).andExpect(status().isOk());
        reader = repository.findById(2).get();
        assertThat(reader.getRole().name()).isEqualTo("USER");

        mockMvc.perform(put("/users/2/grantReaderRole")).andExpect(status().isOk());
        reader = repository.findById(2).get();
        assertThat(reader.getRole().name()).isEqualTo("READER");

        mockMvc.perform(put("/users/2/grantLibrarianRole")).andExpect(status().isOk());
        reader = repository.findById(2).get();
        assertThat(reader.getRole().name()).isEqualTo("LIBRARIAN");
    }
}
