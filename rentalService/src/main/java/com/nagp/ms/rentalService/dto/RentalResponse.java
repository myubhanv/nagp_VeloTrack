package com.nagp.ms.rentalService.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalResponse {
    private Long rentalId;
    private String username;
    private String bikeId;
    private String status;
    private String pickUpLongitude;
    private String pickUpLatitude;
}
