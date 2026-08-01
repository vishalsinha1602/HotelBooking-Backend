package com.project.hotelbooking.controller;

import com.project.hotelbooking.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    // verifying that api call is made by stripe only
    @PostMapping("/payments")
    public ResponseEntity<Void> capturePayments (@RequestBody String payload , @RequestHeader("Stripe-Signature")  String signHeader) {

        try{
            Event event = Webhook.constructEvent(payload,signHeader,endpointSecret);
            bookingService.capturePayment(event);
            return ResponseEntity.noContent().build();

        }catch (SignatureVerificationException ex){

            throw new RuntimeException(ex);

        }

    }
}
