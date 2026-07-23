package com.project.hotelbooking.repository;

import com.project.hotelbooking.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}