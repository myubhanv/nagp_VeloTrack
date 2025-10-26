package com.nagp.ms.rentalService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @Column(nullable=false, length=50)
    private String username;

    @Column(nullable=false, length=50)
    private String bikeId;

    @Column(nullable=false)
    private String status; // ACTIVE / ENDED

    @Column(nullable=false, length=50)
    private String pickUpLongitude;

    @Column(nullable=false, length=50)
    private String pickUpLatitude;

    @Column(nullable = false, updatable = false)
    private LocalDateTime startTime;

    @Column()
    private LocalDateTime endTime;

    @PrePersist
    public void prePersist() {
        startTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        endTime = LocalDateTime.now();
    }
}
