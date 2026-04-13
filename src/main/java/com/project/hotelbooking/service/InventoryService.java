package com.project.hotelbooking.service;

import com.project.hotelbooking.entity.Room;

public interface InventoryService  {

    void initializeRoomForAYear(Room room);
    void deleteFutureInventories(Room room);
}
