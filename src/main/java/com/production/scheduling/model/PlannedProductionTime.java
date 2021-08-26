package com.production.scheduling.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class PlannedProductionTime {
    private LocalDateTime planStart;
    private LocalDateTime planEnd;

    public PlannedProductionTime() {}

    public PlannedProductionTime(LocalDateTime planStart, LocalDateTime planEnd) {
        this.planStart = planStart;
        this.planEnd = planEnd;
    }

    public LocalDateTime getPlanStart() {
        return planStart;
    }

    public void setPlanStart(LocalDateTime planStart) {
        this.planStart = planStart;
    }

    public LocalDateTime getPlanEnd() {
        return planEnd;
    }

    public void setPlanEnd(LocalDateTime planEnd) {
        this.planEnd = planEnd;
    }
}
