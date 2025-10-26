package com.nagp.ms.rentalService.dao;

import com.nagp.ms.rentalService.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalDAO extends JpaRepository<Rental, Long> {
    Optional<Rental> findByUsername(String username);
}
