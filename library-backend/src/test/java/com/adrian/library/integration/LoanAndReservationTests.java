package com.adrian.library.integration;

import com.adrian.library.loan.Loan;
import com.adrian.library.loan.LoanRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoanAndReservationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LoanRepository loanRepository;
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
    void should_create_a_loan_from_a_reservation_after_a_copy_turned_available_due_to_the_cancelling_of_a_loan()
            throws Exception {
        mockMvc.perform(post("/loans")
                .param("userId", "4")
                .param("editionId", "1"))
                .andExpect(status().isOk());

        assertThat(loanRepository.findById(1).get().getStatus().getName()).isEqualTo("oczekujące");

        mockMvc.perform(post("/reservations")
                .param("userId", "5")
                .param("bookId", "1"))
                .andExpect(status().isOk());

        assertThat(reservationRepository.findById(1).get().getStatus().getName()).isEqualTo("aktywna");

        mockMvc.perform(put("/loans/1/cancel"));

        assertThat(reservationRepository.findById(1).get().getStatus().getName()).isEqualTo("zakończona");
        Loan loan = loanRepository.findById(2).get();
        assertThat(loan.getReservation().getId()).isEqualTo(1);
        assertThat(loan.getStatus().getName()).isEqualTo("oczekujące");
        assertThat(loan.getCopy().getEdition().getBook().getId()).isEqualTo(1);
    }
}