package com.nagp.ms.rentalService.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalRequest {
    private String username;
    private String bikeId;
    private String pickUpLongitude;
    private String pickUpLatitude;
}
