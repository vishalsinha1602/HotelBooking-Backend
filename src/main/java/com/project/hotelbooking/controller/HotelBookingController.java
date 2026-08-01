package com.project.hotelbooking.controller;

import com.project.hotelbooking.dto.BookingDto;
import com.project.hotelbooking.dto.BookingRequest;
import com.project.hotelbooking.dto.GuestDto;
import com.project.hotelbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@RequestBody List<GuestDto> guestDtoList, @PathVariable Long bookingId){

        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDtoList));
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String, String>> initiatePayment(@PathVariable Long bookingId){
        String sessionUrl = bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl",sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }


    // to get the current status of payment
    @PostMapping("/{bookingId}/status")
    public ResponseEntity<Map<String, String>>getBookingStatus(@PathVariable Long bookingId){
        return ResponseEntity.ok(Map.of("Status: ",bookingService.getBookingStatus(bookingId)));
    }



}
