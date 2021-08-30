package com.production.scheduling.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ScheduleItem {

    private LocalDateTime planStart;
    private String description;
    private Long workplaceId;
    private Long operationId;

    public ScheduleItem() {}

    public ScheduleItem(LocalDateTime planStart, String description, Long workplaceId, Long operationId) {
        this.planStart = planStart;
        this.description = description;
        this.workplaceId = workplaceId;
        this.operationId = operationId;
    }

    public LocalDateTime getPlanStart() {
        return planStart;
    }

    public void setPlanStart(LocalDateTime planStart) {
        this.planStart = planStart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleItem that = (ScheduleItem) o;
        return Objects.equals(planStart, that.planStart) && Objects.equals(description, that.description) && Objects.equals(workplaceId, that.workplaceId) && Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planStart, description, workplaceId, operationId);
    }

    @Override
    public String toString() {
        return "ScheduleItem{" +
                "planStart=" + planStart +
                ", description='" + description + '\'' +
                ", workplaceId=" + workplaceId +
                ", operationId=" + operationId +
                '}';
    }
}
