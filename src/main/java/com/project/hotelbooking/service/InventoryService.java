package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.HotelDto;
import com.project.hotelbooking.dto.HotelPriceDto;
import com.project.hotelbooking.dto.HotelSearchRequest;
import com.project.hotelbooking.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService  {

    void initializeRoomForAYear(Room room);
    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
