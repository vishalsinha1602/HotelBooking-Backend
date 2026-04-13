package com.project.hotelbooking.controller;

import com.project.hotelbooking.advice.ApiResponse;
import com.project.hotelbooking.dto.HotelDto;
import com.project.hotelbooking.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;

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

    @PatchMapping("/{hotelId}")
    public ResponseEntity<Void> activateHotel(@PathVariable Long hotelId) {

        hotelService.activateHotel(hotelId);

        return ResponseEntity.noContent().build();
    }

}
