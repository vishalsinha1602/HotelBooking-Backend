package com.project.hotelbooking.strategy;

import com.project.hotelbooking.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        boolean  isTodayHoliday= true; // todo: call an api or check with local date

        if(isTodayHoliday){
            price=price.multiply(BigDecimal.valueOf(1.25));
        }

        return price;
    }
}
