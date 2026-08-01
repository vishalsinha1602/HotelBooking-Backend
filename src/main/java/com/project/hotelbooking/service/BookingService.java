package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.BookingDto;
import com.project.hotelbooking.dto.BookingRequest;
import com.project.hotelbooking.dto.GuestDto;
import com.stripe.model.Event;

import java.util.List;
import java.util.Map;


public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);
}
