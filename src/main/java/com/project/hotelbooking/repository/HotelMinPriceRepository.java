package com.project.hotelbooking.repository;

import com.project.hotelbooking.dto.HotelPriceDto;
import com.project.hotelbooking.entity.Hotel;
import com.project.hotelbooking.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice,Long> {

    @Query("""
SELECT new com.project.hotelbooking.dto.HotelPriceDto(i.hotel, AVG(i.price))
FROM HotelMinPrice i
WHERE i.hotel.city = :city
AND i.date BETWEEN :startDate AND :endDate
AND i.hotel.active = true
AND (:roomsCount IS NULL OR 1=1)
GROUP BY i.hotel
HAVING (:dateCount IS NULL OR COUNT(i.date) >= 1)
""")
    Page<HotelPriceDto> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );

    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);;


}
