package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.BookingRequest;
import com.project.hotelbooking.dto.HotelInfoDto;
import com.project.hotelbooking.dto.HotelDto;
import org.springframework.data.domain.Page;

import java.util.List;


public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel( Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);

    List<HotelDto> getAllHotels();


}