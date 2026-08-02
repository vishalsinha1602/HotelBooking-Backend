package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.BookingDto;
import com.project.hotelbooking.dto.BookingRequest;
import com.project.hotelbooking.dto.GuestDto;
import com.project.hotelbooking.dto.HotelReportDto;
import com.stripe.model.Event;

import java.time.LocalDate;
import java.util.List;


public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingDto> getBookingsByHotelId(Long hotelId) ;

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();
}
