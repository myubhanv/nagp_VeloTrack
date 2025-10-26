package com.nagp.ms.rentalService.service;

import com.nagp.ms.rentalService.dao.RentalDAO;
import com.nagp.ms.rentalService.dto.RentalRequest;
import com.nagp.ms.rentalService.dto.RentalResponse;
import com.nagp.ms.rentalService.event.RentalStartEvent;
import com.nagp.ms.rentalService.model.Rental;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalDAO rentalRepository;
    //private final KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public RentalResponse startRental(RentalRequest request) {
        Rental rental = new Rental();
        rental.setUsername(request.getUsername());
        rental.setBikeId(request.getBikeId());
        rental.setPickUpLatitude(request.getPickUpLatitude());
        rental.setPickUpLongitude(request.getPickUpLongitude());
        rental.setStartTime(LocalDateTime.now());
        rental.setStatus("ACTIVE");
        rentalRepository.save(rental);

        // Publish event to Kafka
       kafkaTemplate.send("rental.started", String.valueOf(rental.getBikeId()));

        return getRentalResponse(rental);
    }

    public ResponseEntity<?> endRental(Long rentalId) {

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rental info not found"));
        }

        Rental rental = optionalRental.get();
        if (rental.getEndTime() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Rental already ended"));
        }
        rental.setEndTime(LocalDateTime.now());
        rental.setStatus("ENDED");
        rentalRepository.save(rental);

       kafkaTemplate.send("rental.ended", String.valueOf(rental.getBikeId()));

        return ResponseEntity.ok(getRentalResponse(rental));
    }

    public List<RentalResponse> getRentalsByUser(String username) {
        return rentalRepository.findByUsername(username).stream()
                .map(this::getRentalResponse)
                .collect(Collectors.toList());
    }

    public RentalResponse getRentalResponse(Rental rental) {
       return new RentalResponse(rental.getRentalId(), rental.getUsername(), rental.getBikeId(), rental.getStatus(), rental.getPickUpLongitude(), rental.getPickUpLatitude());
    }

    public RentalStartEvent createRentalStartEvent(Rental rental){
        return RentalStartEvent.builder()
                .rentalId(rental.getRentalId())
                .bikeId(rental.getBikeId())
                .username(rental.getUsername())
                .startTime(LocalDateTime.now().toString())
                .build();
    }
}
