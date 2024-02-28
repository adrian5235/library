package com.adrian.library.reservation.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.adrian.library.reservation.Reservation;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "reservation_statuses")
public class ReservationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "status")
    private List<Reservation> reservations = new ArrayList<>();
}
