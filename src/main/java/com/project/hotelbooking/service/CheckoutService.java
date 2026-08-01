package com.project.hotelbooking.service;

import com.project.hotelbooking.entity.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);



}
