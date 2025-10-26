package com.nagp.ms.bikeService.service;

import com.nagp.ms.bikeService.dao.BikeDAO;
import com.nagp.ms.bikeService.dto.BikeRequest;
import com.nagp.ms.bikeService.model.Bike;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BikeService {
    private final BikeDAO bikeRepository;

    public List<Bike> findNearbyBikes(double lat, double lon, double radius) {
        // Example pseudo-query
        return bikeRepository.findNearbyBikes(lat, lon, radius);
    }

    public List<Bike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public Bike addBike(BikeRequest request) {
        Bike bike = Bike.builder()
                .model(request.getModel())
                .bikeCondition(Bike.BikeCondition.GOOD)
                .status(Bike.Status.AVAILABLE)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        return bikeRepository.save(bike);
    }

    public ResponseEntity<?> updateBikeStatus(Long bikeId, String newStatus) {
        Optional<Bike> optionalBike = bikeRepository.findById(bikeId);
        if (optionalBike.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Bike not found"));
        }

        Bike bike = optionalBike.get();
        bike.setStatus(Bike.Status.valueOf(newStatus));
        bikeRepository.save(bike);
        return ResponseEntity.ok(Map.of("message", "Bike status updated successfully"));
    }

    public ResponseEntity<?> updateBikeCondition(Long bikeId, String newCondition) {
        Optional<Bike> optionalBike = bikeRepository.findById(bikeId);
        if (optionalBike.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Bike not found"));
        }

        Bike bike = optionalBike.get();
        bike.setBikeCondition(Bike.BikeCondition.valueOf(newCondition));
        bikeRepository.save(bike);
        return ResponseEntity.ok(Map.of("message", "Bike condition updated successfully"));
    }
}
