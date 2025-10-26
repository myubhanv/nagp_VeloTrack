package com.nagp.ms.rentalService.controller;

import com.nagp.ms.rentalService.dto.RentalResponse;
import com.nagp.ms.rentalService.dto.RentalRequest;
import com.nagp.ms.rentalService.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("rentals")
public class RentalController {

    @Autowired
    RentalService rentalService;

    @PostMapping
    public ResponseEntity<RentalResponse> startRental(@RequestBody RentalRequest request) {
        RentalResponse response = rentalService.startRental(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{rentalId}/end")
    public ResponseEntity<?> endRental(@PathVariable Long rentalId) {
        return rentalService.endRental(rentalId);
    }

    @GetMapping("/details/{username}")
    public ResponseEntity<List<RentalResponse>> getRentalHistory(@PathVariable String username) {
        List<RentalResponse> history = rentalService.getRentalsByUser(username);
        return ResponseEntity.ok(history);
    }

}
