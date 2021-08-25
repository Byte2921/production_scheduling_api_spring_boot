package com.production.scheduling.logic;

import java.time.Duration;
import java.time.LocalDateTime;

public class ProductLogic {
    public static Long calculateNewDuration(LocalDateTime planStart, LocalDateTime planEnd) {
        return Duration.between(planStart, planEnd).getSeconds() * 60;
    }
}
