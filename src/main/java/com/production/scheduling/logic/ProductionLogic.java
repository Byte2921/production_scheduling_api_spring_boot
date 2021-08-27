package com.production.scheduling.logic;

import com.production.scheduling.model.PlannedProductionTime;
import com.production.scheduling.model.Product;
import com.production.scheduling.model.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public final class ProductionLogic {

    public static Long calculateWorkTimeLength(LocalDateTime planStart, LocalDateTime planEnd) {
        return Duration.between(planStart, planEnd).getSeconds() / 60;
    }

    public static Product createNewProduct(Product product) {
        product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
        product.setCreated(LocalDateTime.now());
        product.setCreatedBy(System.getProperty("user.name"));
        return product;
    }

    public static Product updateProductTimeSpan(PlannedProductionTime dates, Product product) {
        product.setPlanStart(dates.getPlanStart());
        product.setPlanEnd(dates.getPlanEnd());
        product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
        updateLastModified(product);
        return product;
    }

    public static void updateLastModified(Product product) {
        product.setModified(LocalDateTime.now());
        product.setModifiedBy(System.getProperty("user.name"));
    }

    public static Product start(Product product) {
        product.setStatus(Status.IN_PROGRESS);
        product.setActualStart(LocalDateTime.now());
        updateLastModified(product);
        return product;
    }

    public static Product finish(Product product) {
        product.setStatus(Status.COMPLETED);
        product.setActualEnd(LocalDateTime.now());
        product.setActualDuration(calculateWorkTimeLength(product.getActualStart(), product.getActualEnd()));
        updateLastModified(product);
        return product;
    }

    public static Product cancel(Product product) {
        product.setStatus(Status.CANCELED);
        updateLastModified(product);
        return product;
    }

    public static Product undoLastAction(Product product) {
        product.setStatus(product.getStatus().previous());
        updateLastModified(product);
        return product;
    }
}
