package com.nagp.ms.bikeService.controller;

import com.nagp.ms.bikeService.dto.BikeRequest;
import com.nagp.ms.bikeService.model.Bike;
import com.nagp.ms.bikeService.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("bikes")
public class BikeController {
    @Autowired
    BikeService bikeService;

    @GetMapping
    public ResponseEntity<List<Bike>> searchBikes(
            @RequestParam(required = false) String status,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5000") double radius) {

        List<Bike> bikes = bikeService.findNearbyBikes(lat, lon, radius);
        return ResponseEntity.ok(bikes);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Bike>> getAllBikes() {
        List<Bike> bikes = bikeService.getAllBikes();
        return ResponseEntity.ok(bikes);
    }

    @PostMapping
    public ResponseEntity<Bike> addBike(@RequestBody BikeRequest request) {
        Bike bike = bikeService.addBike(request);
        return ResponseEntity.ok(bike);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateBikeStatus(@PathVariable Long id,
            @RequestParam String status) {
        return bikeService.updateBikeStatus(id, status);
    }

    //to be used from IOT sensors
    @PatchMapping("/{id}/condition")
    public ResponseEntity<?> updateBikeCondition(@PathVariable Long id,
            @RequestParam String condition) {
        return bikeService.updateBikeCondition(id, condition);
    }
}