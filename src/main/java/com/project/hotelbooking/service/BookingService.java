package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.BookingDto;
import com.project.hotelbooking.dto.BookingRequest;
import com.project.hotelbooking.dto.GuestDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
