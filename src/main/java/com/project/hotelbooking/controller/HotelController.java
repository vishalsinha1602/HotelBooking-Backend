package com.project.hotelbooking.controller;

import com.project.hotelbooking.advice.ApiResponse;
import com.project.hotelbooking.dto.*;
import com.project.hotelbooking.service.BookingService;
import com.project.hotelbooking.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto) {
        log.info("Attempting to create new hotel"+hotelDto.getName());

        HotelDto hotel = hotelService.createNewHotel(hotelDto);

        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId) {

        HotelDto hotelDto = hotelService.getHotelById(hotelId);


        return ResponseEntity.ok(hotelDto);

    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(
            @PathVariable Long hotelId,
            @RequestBody HotelDto hotelDto) {

        log.info("Attempting to update hotel with id: {}", hotelId);

        HotelDto updatedHotel = hotelService.updateHotelById(hotelId, hotelDto);

        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId) {

        log.info("Attempting to delete hotel with id: {}", hotelId);

        hotelService.deleteHotelById(hotelId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<Void> activateHotel(@PathVariable Long hotelId) {

        hotelService.activateHotel(hotelId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels(){

        List<HotelDto> hotel = hotelService.getAllHotels();

        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<List<BookingDto>> getBookingsByHotelId(@PathVariable Long hotelId){

        return ResponseEntity.ok(bookingService.getBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    @Operation(summary = "Generate a bookings report of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }







}
