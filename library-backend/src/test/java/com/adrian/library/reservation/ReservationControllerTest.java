package com.adrian.library.reservation;

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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationControllerTest {

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
    void shouldNotBeGivenAccessToOtherUserReservations() throws Exception {
        mockMvc.perform(get("/reservations/user/4")).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("librarian@localhost")
    void shouldBeGivenAccessToOtherUserReservations() throws Exception {
        mockMvc.perform(get("/reservations/user/4")).andExpect(status().isOk());
    }

    @Test
    @Order(1)
    @WithUserDetails("reader@localhost")
    void shouldReserveBookAndDecreaseActionPoints() throws Exception {
        int actionPoints = userRepository.findById(1).get().getActionPoints();

        mockMvc.perform(post("/reservations")
                        .param("bookId", "3"))
                .andExpect(status().isOk());

        assertThat(reservationRepository.getReferenceById(1)).isNotNull();
        assertThat(userRepository.findById(1).get().getActionPoints()).isEqualTo(actionPoints - 1);
    }

    @Test
    @Order(2)
    @WithUserDetails("reader@localhost")
    void shouldBeGivenAccessToTheirOwnReservationsAndReturnThem() throws Exception {
        mockMvc.perform(get("/reservations/user/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].book.id", is(3)));
    }

    @Test
    @Order(3)
    @WithUserDetails("reader@localhost")
    void shouldNotReserveBookDueToGivenBookAlreadyBeingReservedByTheGivenUser() throws Exception {
        mockMvc.perform(post("/reservations")
                        .param("bookId", "3"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails("admin@localhost")
    void shouldNotReserveBookDueToTheLackOfActionPoints() throws Exception {
        User user = userRepository.findById(3).get();
        user.setActionPoints(0);
        userRepository.save(user);

        mockMvc.perform(post("/reservations")
                        .param("bookId", "2")
                        .param("editionId", "2"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails("reader@localhost")
    void shouldBorrowBook_shouldNotReserveBookDueToGivenBookAlreadyBeingBorrowedByTheGivenUser() throws Exception {
        mockMvc.perform(post("/borrowings")
                .param("editionId", "1"));

        mockMvc.perform(post("/reservations")
                        .param("bookId", "1"))
                .andExpect(status().isConflict());
    }
}