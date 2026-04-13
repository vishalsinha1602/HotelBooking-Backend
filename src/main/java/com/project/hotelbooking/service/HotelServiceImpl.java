package com.project.hotelbooking.service;

import com.project.hotelbooking.dto.HotelDto;
import com.project.hotelbooking.entity.Hotel;
import com.project.hotelbooking.entity.Room;
import com.project.hotelbooking.exception.ResourceNotFoundException;
import com.project.hotelbooking.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a Hotel with name : {}", hotelDto);
        //converting hotelDto->HotelEntity
        Hotel hotel = modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);
        hotel =hotelRepository.save(hotel);
        log.info("Created a new Hotel with name : {}", hotelDto.getId());
        return modelMapper.map(hotel,HotelDto.class) ;
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting Hotel with id : {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Hotel with id : " + id + " not found"));
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {

        log.info("Updating hotel with id: {}", id);

        // 1. Existing hotel fetch
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel with id: " + id + " not found")
                );
        // 2. DTO → Existing Entity
        modelMapper.map(hotelDto, hotel);

        hotel.setId(id);
        // 3. Save
        hotel = hotelRepository.save(hotel);

        // 4. Return DTO
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {

        log.info("Deleting Hotel with id : {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel with id: " + id + " not found")
                );

        // 2. Delete karo
        hotelRepository.deleteById(id);

        for(Room room: hotel.getRooms()){
            inventoryService.deleteFutureInventories(room);
        }


    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating Hotel with id : {}", hotelId);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel with id: " + hotelId + " not found")
                );
        hotel.setActive(true);
        hotelRepository.save(hotel);

        //assuming only do it once
        for(Room room: hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }


    }
}
