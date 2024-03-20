package com.adrian.library.borrowing;

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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BorrowingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BorrowingRepository borrowingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;

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
    void shouldNotBeGivenAccessToOtherUserBorrowings() throws Exception {
        mockMvc.perform(get("/borrowings/user/4")).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails("librarian@localhost")
    void shouldBeGivenAccessToOtherUserBorrowings() throws Exception {
        mockMvc.perform(get("/borrowings/user/4")).andExpect(status().isOk());
    }

    @Test
    @Order(1)
    @WithUserDetails("reader@localhost")
    void shouldBorrowBookAndDecreaseActionPoints() throws Exception {
        int actionPoints = userRepository.findById(1).get().getActionPoints();

        mockMvc.perform(post("/borrowings")
                        .param("editionId", "1"))
                .andExpect(status().isOk());

        assertThat(borrowingRepository.getReferenceById(1)).isNotNull();
        assertThat(userRepository.findById(1).get().getActionPoints()).isEqualTo(actionPoints - 1);
    }

    @Test
    @Order(2)
    @WithUserDetails("reader@localhost")
    void shouldBeGivenAccessToTheirOwnBorrowingsAndReturnThem() throws Exception {
        mockMvc.perform(get("/borrowings/user/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].copy.edition.id", is(1)));
    }

    @Test
    @Order(3)
    @WithUserDetails("reader@localhost")
    void shouldNotBorrowBookDueToGivenBookAlreadyBeingBorrowedByTheGivenUser() throws Exception {
        mockMvc.perform(post("/borrowings")
                        .param("editionId", "1"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails("librarian@localhost")
    void shouldNotBorrowBookDueToTheLackOfActionPoints() throws Exception {
        User user = userRepository.findById(5).get();
        user.setActionPoints(0);
        userRepository.save(user);

        mockMvc.perform(post("/borrowings")
                        .param("userId", "5")
                        .param("editionId", "4"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails("reader@localhost")
    void shouldReserveBook_shouldNotBorrowBookDueToGivenBookAlreadyBeingReservedByTheGivenUser() throws Exception {
        mockMvc.perform(post("/reservations")
                        .param("bookId", "2")
                        .param("editionId", "2"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/borrowings")
                        .param("editionId", "3")
                        .param("userId", "1"))
                .andExpect(status().isConflict());
    }
}