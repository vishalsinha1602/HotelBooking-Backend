package com.project.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDto {

    private Long hotelId;
    private String hotelName;
    private String city;
    private String[] photos;
    private String[] amenities;
    private Double price;
}