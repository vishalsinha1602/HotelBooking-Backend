package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.ProfileUpdateRequestDto;
import com.project.hotelbooking.dto.UserDto;
import com.project.hotelbooking.entity.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();
}
