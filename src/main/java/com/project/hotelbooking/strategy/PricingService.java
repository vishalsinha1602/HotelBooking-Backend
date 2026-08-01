package com.project.hotelbooking.strategy;

import com.project.hotelbooking.entity.Inventory;
import com.project.hotelbooking.entity.Room;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class PricingService {

    public BigDecimal calculateDynamicPricing(Inventory inventory) {
        PricingStrategy pricingStrategy = new BasePricingStrategy();

        // apply the additional strategy
        pricingStrategy = new SurgePricingStrategy(pricingStrategy);
        pricingStrategy = new OccupancyPricingStrategy(pricingStrategy);
        pricingStrategy = new UrgencyPricingStrategy(pricingStrategy);
        pricingStrategy = new HolidayPricingStrategy(pricingStrategy);


        return pricingStrategy.calculatePrice(inventory);
    }

    public BigDecimal calculateTotalPrice(List<Inventory> inventory) {
        //return the sum of the price of this inventory
        BigDecimal totalPrice = inventory.stream()
                .map(this::calculateDynamicPricing)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPrice;

    }


}
