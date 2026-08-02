package com.project.hotelbooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequestDto {
    private String name;
    private LocalDate email;
    private String password;
}
