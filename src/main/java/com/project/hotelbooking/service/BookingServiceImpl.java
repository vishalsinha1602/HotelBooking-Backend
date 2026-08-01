package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.BookingDto;
import com.project.hotelbooking.dto.BookingRequest;
import com.project.hotelbooking.dto.GuestDto;
import com.project.hotelbooking.entity.*;
import com.project.hotelbooking.entity.enums.BookingStatus;
import com.project.hotelbooking.exception.ResourceNotFoundException;
import com.project.hotelbooking.exception.UnAuthorisedException;
import com.project.hotelbooking.repository.*;
import com.project.hotelbooking.strategy.PricingService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final CheckoutService checkoutService;
    private final PricingService pricingService;


    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public BookingDto initialiseBooking(BookingRequest bookingRequest) {

        log.info("Initialising booking for hotel : {}, room: {}, date {}-{}", bookingRequest.getHotelId(),
                bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with id: "+bookingRequest.getHotelId()));

        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(() ->
                new ResourceNotFoundException("Room not found with id: "+bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())+1;

        if (inventoryList.size() != daysCount) {
            throw new IllegalStateException("Room is not available anymore");
        }


        // Reserve the room/ update the booked count of inventories
        inventoryRepository.initBooking(room.getId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate(),bookingRequest.getRoomsCount());



//         calculate dynamic amount -> done
        BigDecimal priceForOneRoom = pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomsCount()));



        // Create the Booking
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingRequest.getRoomsCount())
                .amount(totalPrice)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {

        log.info("Adding guests for booking with id: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResourceNotFoundException("Booking not found with id: "+bookingId));


        User user = getCurrentUser();

        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if (hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }

        for (GuestDto guestDto: guestDtoList) {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(user);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }







    public boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    //Payment Service
    @Override
    @Transactional
    public String initiatePayments(Long bookingId) {

        //first checking if booking id exists or not
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        User user = getCurrentUser();

        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if (hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has already expired");
        }

        String sessionUrl = checkoutService.getCheckoutSession(booking,frontendUrl+"/payments/success",frontendUrl+"/payments/failure");

        booking.setBookingStatus(BookingStatus.PAYMENTS_PENDING);
        booking = bookingRepository.save(booking);
        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session == null) return;

            String sessionId = session.getId();
            Booking booking =
                    bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(() ->
                            new ResourceNotFoundException("Booking not found for session ID: "+sessionId));

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),booking.getCheckInDate(),
                    booking.getCheckOutDate(),booking.getRoomsCount());

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomsCount());

            inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomsCount());

            log.info("Successfully confirmed the booking for Booking ID: {}", booking.getId());
        } else {
            log.warn("Unhandled event type: {}", event.getType());
        }


    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));


        User user = getCurrentUser();

        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only Confirmed Booking can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),booking.getCheckInDate(),
                booking.getCheckOutDate(),booking.getRoomsCount());

        inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomsCount());


        // now handle refunds

        try {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        User user = getCurrentUser();

        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }


        return booking.getBookingStatus().name();
    }

}
