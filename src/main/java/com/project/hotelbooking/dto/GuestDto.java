package com.project.hotelbooking.dto;

import com.project.hotelbooking.entity.User;
import com.project.hotelbooking.entity.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {

    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
