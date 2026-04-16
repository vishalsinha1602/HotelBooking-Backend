package com.project.hotelbooking.repository;

import com.project.hotelbooking.entity.Inventory;
import com.project.hotelbooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    void deleteByRoom( Room room);
}