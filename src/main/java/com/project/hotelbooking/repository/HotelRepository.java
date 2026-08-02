package com.project.hotelbooking.repository;

import com.project.hotelbooking.entity.Hotel;
import com.project.hotelbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByOwner(User currentUser);
}