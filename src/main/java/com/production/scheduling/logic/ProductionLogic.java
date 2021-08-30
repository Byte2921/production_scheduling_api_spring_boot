package com.production.scheduling.logic;

import com.production.scheduling.LoadDatabase;
import com.production.scheduling.model.*;
import com.production.scheduling.repository.OperationRepository;
import com.production.scheduling.repository.WorkplaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ProductionLogic {

    @Autowired
    OperationRepository operationRepository;
    @Autowired
    WorkplaceRepository workplaceRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadDatabase.class);

    public ProductionLogic() {}

    public Long calculateWorkTimeLength(LocalDateTime planStart, LocalDateTime planEnd) {
        return Duration.between(planStart, planEnd).getSeconds() / 60;
    }

    public Product createNewProduct(ScheduleItem scheduleItem) {
        Product product = new Product();
        product.setPlanStart(scheduleItem.getPlanStart());
        product.setPlanEnd(scheduleItem.getPlanStart().plusMinutes(100));
        product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
        product.setDescription(scheduleItem.getDescription());
        assignWorkplace(product, scheduleItem.getWorkplaceId());
        assignOperation(product, scheduleItem.getOperationId());
        signNewProduct(product);
        return product;
    }

    public Product updateProductTimeSpan(PlannedProductionTime dates, Product product) {
        product.setPlanStart(dates.getPlanStart());
        product.setPlanEnd(dates.getPlanEnd());
        product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
        updateLastModified(product);
        return product;
    }

    private void signNewProduct(Product product) {
        product.setCreated(LocalDateTime.now());
        product.setCreatedBy(System.getProperty("user.name"));
    }

    private void updateLastModified(Product product) {
        product.setModified(LocalDateTime.now());
        product.setModifiedBy(System.getProperty("user.name"));
    }

    private void assignWorkplace(Product product, Long workplaceId) {
        Workplace workplace = workplaceRepository.getById(workplaceId);
        product.setWorkplace(workplaceRepository.getById(workplaceId));
    }

    private void assignOperation(Product product, Long operationId) {
        product.setOperation(operationRepository.getById(operationId));
    }

    public Product start(Product product) {
        product.setStatus(Status.IN_PROGRESS);
        product.setActualStart(LocalDateTime.now());
        updateLastModified(product);
        return product;
    }

    public Product finish(Product product) {
        product.setStatus(Status.COMPLETED);
        product.setActualEnd(LocalDateTime.now());
        product.setActualDuration(calculateWorkTimeLength(product.getActualStart(), product.getActualEnd()));
        updateLastModified(product);
        return product;
    }

    public Product cancel(Product product) {
        product.setStatus(Status.CANCELED);
        updateLastModified(product);
        return product;
    }

    public Product undoLastAction(Product product) {
        product.setStatus(product.getStatus().previous());
        updateLastModified(product);
        return product;
    }
}
