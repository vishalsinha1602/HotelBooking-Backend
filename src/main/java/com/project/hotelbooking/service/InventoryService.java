package com.project.hotelbooking.service;

import com.project.hotelbooking.entity.Room;

public interface InventoryService  {

    void initializeRoomForAYear(Room room);
    void deleteAllInventories(Room room);
}
