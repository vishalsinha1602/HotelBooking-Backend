package com.project.hotelbooking.strategy;

import com.project.hotelbooking.entity.Inventory;


import java.math.BigDecimal;


public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
