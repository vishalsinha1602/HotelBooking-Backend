package com.project.hotelbooking.controller;

import com.project.hotelbooking.dto.HotelDto;
import com.project.hotelbooking.dto.HotelInfoDto;
import com.project.hotelbooking.dto.HotelPriceDto;
import com.project.hotelbooking.dto.HotelSearchRequest;
import com.project.hotelbooking.service.HotelService;
import com.project.hotelbooking.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {

        var page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId) {

        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }


}
