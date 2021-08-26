package com.production.scheduling.logic;

import com.production.scheduling.model.Product;

import java.time.Duration;
import java.time.LocalDateTime;

public class ProductionLogic {

    public static Long calculateWorkTimeLength(LocalDateTime planStart, LocalDateTime planEnd) {
        return Duration.between(planStart, planEnd).getSeconds() / 60;
    }

    public static Product CreateNewProduct(Product p) {
        Product product = p;
        product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
        product.setCreated(LocalDateTime.now());
        product.setCreatedBy(System.getProperty("user.name"));
        return product;
    }
}
