package com.project.hotelbooking.repository;

import com.project.hotelbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByPaymentSessionId(String sessionId);
}
