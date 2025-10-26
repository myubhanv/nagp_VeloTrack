package com.nagp.ms.rentalService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalStartEvent {

    private Long rentalId;
    private String bikeId;
    private String username;
    private String startTime;
}
