package com.nagp.ms.bikeService.dao;

import com.nagp.ms.bikeService.model.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BikeDAO extends JpaRepository<Bike, Long> {

    @Query(value = """
        SELECT *,
               (6371000 * acos(
                   cos(radians(:lat)) * cos(radians(latitude)) *
                   cos(radians(longitude) - radians(:lon)) +
                   sin(radians(:lat)) * sin(radians(latitude))
               )) AS distance
        FROM bikes
        HAVING distance <= :radius
        ORDER BY distance
        """, nativeQuery = true)
    List<Bike> findNearbyBikes(double lat, double lon, double radius);

}
