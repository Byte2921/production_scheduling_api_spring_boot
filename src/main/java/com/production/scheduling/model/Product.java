package com.production.scheduling.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})} )
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private LocalDateTime planStart;
    private LocalDateTime planEnd;
    private Long planDuration;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private Long actualDuration;
    private String description;
    @OneToOne
    @JoinColumn(name = "workplace_id", referencedColumnName = "id")
    private Workplace workplace;
    @OneToOne
    @JoinColumn(name = "operation_id", referencedColumnName = "id")
    private Operation operation;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime modified;
    private String modifiedBy;

    public Product() {}

    public Product(LocalDateTime planStart, LocalDateTime planEnd, Long planDuration, LocalDateTime actualStart, LocalDateTime actualEnd,
                   Long actualDuration, String description, Workplace workplace, Operation operation, LocalDateTime created, String createdBy,
                   LocalDateTime modified, String modifiedBy) {
        this.planStart = planStart;
        this.planEnd = planEnd;
        this.planDuration = planDuration;
        this.actualStart = actualStart;
        this.actualEnd = actualEnd;
        this.actualDuration = actualDuration;
        this.description = description;
        this.workplace = workplace;
        this.operation = operation;
        this.created = created;
        this.createdBy = createdBy;
        this.modified = modified;
        this.modifiedBy = modifiedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(Long planDuration) {
        this.planDuration = planDuration;
    }

    public LocalDateTime getActualStart() {
        return actualStart;
    }

    public void setActualStart(LocalDateTime actualStart) {
        this.actualStart = actualStart;
    }

    public LocalDateTime getActualEnd() {
        return actualEnd;
    }

    public void setActualEnd(LocalDateTime actualEnd) {
        this.actualEnd = actualEnd;
    }

    public Long getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Long actualDuration) {
        this.actualDuration = actualDuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(planStart, product.planStart) && Objects.equals(planEnd, product.planEnd) && Objects.equals(planDuration, product.planDuration) && Objects.equals(actualStart, product.actualStart) && Objects.equals(actualEnd, product.actualEnd) && Objects.equals(actualDuration, product.actualDuration) && Objects.equals(description, product.description) && Objects.equals(workplace, product.workplace) && Objects.equals(operation, product.operation) && Objects.equals(created, product.created) && Objects.equals(createdBy, product.createdBy) && Objects.equals(modified, product.modified) && Objects.equals(modifiedBy, product.modifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, planStart, planEnd, planDuration, actualStart, actualEnd, actualDuration, description, workplace, operation, created, createdBy, modified, modifiedBy);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", planStart=" + planStart +
                ", planEnd=" + planEnd +
                ", planDuration=" + planDuration +
                ", actualStart=" + actualStart +
                ", actualEnd=" + actualEnd +
                ", actualDuration=" + actualDuration +
                ", description='" + description + '\'' +
                ", workplace=" + workplace +
                ", operation=" + operation +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", modified=" + modified +
                ", modifiedBy='" + modifiedBy + '\'' +
                '}';
    }
}
