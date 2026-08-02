package com.project.hotelbooking.repository;

import com.project.hotelbooking.dto.BookingDto;
import com.project.hotelbooking.entity.Booking;
import com.project.hotelbooking.entity.Hotel;
import com.project.hotelbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByPaymentSessionId(String sessionId);

    List<Booking> findByHotel(Hotel hotel);

    List<Booking> findByHotelAndCreatedAtBetween(Hotel hotel, LocalDateTime startDateTime, LocalDateTime endDateTime);


    List<Booking> findByUser(User currentUser);
}
