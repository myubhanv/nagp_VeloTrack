package com.nagp.ms.bikeService.eventListener;

import com.nagp.ms.bikeService.dao.BikeDAO;
import com.nagp.ms.bikeService.model.Bike;
import com.nagp.ms.bikeService.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BikeEventListener {

    @Autowired
    private BikeService bikeService;

    @KafkaListener(topics = "rental.started", groupId = "bike-service-group")
    public void handleRentalStarted(String bikeIdStr) {
        System.out.println("Received rental.started event for bikeid: " + bikeIdStr);
        try {
            Long bikeId = Long.parseLong(bikeIdStr.replace("\"", ""));
            bikeService.updateBikeStatus(bikeId, "RENTED");
            System.out.println("Updated bike " + bikeId + " to RENTED");
        } catch (NumberFormatException e) {
            System.err.println("Invalid bikeId received: " + bikeIdStr);
        }

    }

    @KafkaListener(topics = "rental.ended", groupId = "bike-service-group")
    public void handleRentalEnded(String bikeIdStr) {
        System.out.println("Received rental.ended event for bikeid: " + bikeIdStr);
        try {
            Long bikeId = Long.parseLong(bikeIdStr.replace("\"", ""));
            bikeService.updateBikeStatus(bikeId, "AVAILABLE");
            System.out.println("Updated bike " + bikeId + " to AVAILABLE");
        } catch (NumberFormatException e) {
            System.err.println("Invalid bikeId received: " + bikeIdStr);
        }
    }
}
