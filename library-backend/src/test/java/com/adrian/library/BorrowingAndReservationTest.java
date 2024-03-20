package com.adrian.library;

import com.adrian.library.borrowing.Borrowing;
import com.adrian.library.borrowing.BorrowingRepository;
import com.adrian.library.reservation.ReservationRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BorrowingAndReservationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BorrowingRepository borrowingRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Container
    private final static MySQLContainer container = new MySQLContainer("mysql:latest");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    @WithUserDetails("librarian@localhost")
    void shouldCreateBorrowingFromReservationAfterCopyTurnedAvailableDueToTheCancellingOfBorrowing()
            throws Exception {
        mockMvc.perform(post("/borrowings")
                .param("userId", "4")
                .param("editionId", "1"))
                .andExpect(status().isOk());

        assertThat(borrowingRepository.findById(1).get().getStatus().getName()).isEqualTo("awaiting");

        mockMvc.perform(post("/reservations")
                .param("userId", "5")
                .param("bookId", "1"))
                .andExpect(status().isOk());

        assertThat(reservationRepository.findById(1).get().getStatus().getName()).isEqualTo("active");

        mockMvc.perform(put("/borrowings/1/cancel"));

        assertThat(reservationRepository.findById(1).get().getStatus().getName()).isEqualTo("finalized");
        Borrowing borrowing = borrowingRepository.findById(2).get();
        assertThat(borrowing.getReservation().getId()).isEqualTo(1);
        assertThat(borrowing.getStatus().getName()).isEqualTo("awaiting");
        assertThat(borrowing.getCopy().getEdition().getBook().getId()).isEqualTo(1);
    }
}