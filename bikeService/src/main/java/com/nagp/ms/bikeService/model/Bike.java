package com.nagp.ms.bikeService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bikes")
public class Bike {
    public enum Status {
        AVAILABLE, // when rental ends or work order Completes
        RENTED, // when rental starts
        IN_MAINTENANCE, // when maintenance work order created
        OUT_OF_SERVICE
    }

    public enum BikeCondition {
        GOOD,
        MAINTENANCE_REQUIRED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    private double longitude;
    private double latitude;
    private LocalDateTime lastMaintenanceDate;
    private Status status;
    private BikeCondition bikeCondition;

    @PrePersist
    public void onCreate() {
        this.lastMaintenanceDate = LocalDateTime.now();
    }

}
