package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.HotelDto;
import com.project.hotelbooking.entity.Hotel;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel( Long hotelId);

}