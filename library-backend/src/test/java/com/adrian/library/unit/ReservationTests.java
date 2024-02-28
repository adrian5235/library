package com.adrian.library.unit;

import com.adrian.library.reservation.ReservationRepository;
import com.adrian.library.user.User;
import com.adrian.library.user.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;

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
    void should_grant_access_by_id() throws Exception {
        mockMvc.perform(get("/reservations/user/1")).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("reader@localhost")
    void should_deny_access() throws Exception {
        mockMvc.perform(get("/reservations/user/4")).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("librarian@localhost")
    void should_grant_access_by_role() throws Exception {
        mockMvc.perform(get("/reservations/user/1")).andExpect(status().isOk());
    }

    @Test
    @Order(1)
    @WithUserDetails("reader@localhost")
    void should_create_a_reservation() throws Exception {
        mockMvc.perform(post("/reservations")
                .param("userId", "1")
                .param("bookId", "3"))
                .andExpect(status().isOk());

        assertThat(reservationRepository.getReferenceById(1)).isNotNull();
    }

    @Test
    @Order(2)
    @WithUserDetails("reader@localhost")
    void should_not_create_a_reservation_due_to_a_given_book_already_being_reserved_by_the_given_user()
            throws Exception {
        mockMvc.perform(post("/reservations")
                .param("userId", "1")
                .param("bookId", "3"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    @WithUserDetails("reader@localhost")
    void should_not_create_a_reservation_due_to_a_book_already_being_loaned_by_this_user() throws Exception {
        mockMvc.perform(post("/loans")
                .param("userId", "1")
                .param("editionId", "1"));

        mockMvc.perform(post("/reservations")
                .param("userId", "1")
                .param("bookId", "1"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(4)
    @WithUserDetails("librarian@localhost")
    void should_decrease_the_action_points_when_creating_a_reservation() throws Exception {
        assertThat(userRepository.findById(4).get().getActionPoints()).isEqualTo(5);

        mockMvc.perform(post("/reservations")
                .param("userId", "4")
                .param("bookId", "3"))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(4).get().getActionPoints()).isEqualTo(4);
    }

    @Test
    @Order(5)
    @WithUserDetails("librarian@localhost")
    void should_not_create_a_reservation_due_to_the_lack_of_action_points() throws Exception {
        User user = userRepository.findById(4).get();
        user.setActionPoints(0);
        userRepository.save(user);

        mockMvc.perform(post("/reservations")
                .param("userId", "4")
                .param("bookId", "2")
                .param("editionId", "2"))
                .andExpect(status().isConflict());
    }
}