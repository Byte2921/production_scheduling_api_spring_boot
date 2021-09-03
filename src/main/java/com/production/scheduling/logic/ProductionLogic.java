package com.production.scheduling.logic;

import com.production.scheduling.exceptions.ProductNotFoundException;
import com.production.scheduling.model.*;
import com.production.scheduling.repository.OperationRepository;
import com.production.scheduling.repository.ProductRepository;
import com.production.scheduling.repository.WorkplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProductionLogic {

    private static final int DIVIDER = 60;
    private static final long DEFAULT_WORKING_TIME = 100;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private WorkplaceRepository workplaceRepository;
    @Autowired
    private ProductRepository productRepository;

    public ProductionLogic() {
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Long calculateWorkTimeLength(LocalDateTime planStart, LocalDateTime planEnd) {
        return Duration.between(planStart, planEnd).getSeconds() / DIVIDER;
    }

    public Product createNewProduct(ScheduleItem scheduleItem) {
        Product product = new Product();
        product.setPlanStart(scheduleItem.getPlanStart());
        product.setPlanEnd(scheduleItem.getPlanStart().plusMinutes(DEFAULT_WORKING_TIME));
        product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
        product.setDescription(scheduleItem.getDescription());
        assignWorkplace(product, scheduleItem.getWorkplaceId());
        assignOperation(product, scheduleItem.getOperationId());
        signNewProduct(product);
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProductTimeSpan(PlannedProductionTime dates, Long id) {
        Product product = productRepository.getById(id);
        product.setPlanStart(dates.getPlanStart());
        product.setPlanEnd(dates.getPlanEnd());
        product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
        updateLastModified(product);
        return productRepository.save(product);
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
        product.setWorkplace(workplaceRepository.getById(workplaceId));
    }

    private void assignOperation(Product product, Long operationId) {
        product.setOperation(operationRepository.getById(operationId));
    }

    public Product start(Long id) {
        Product product = productRepository.getById(id);
        if (product.getStatus() == Status.WAITING) {
            product.setStatus(Status.IN_PROGRESS);
            product.setActualStart(LocalDateTime.now());
            updateLastModified(product);
            return productRepository.save(product);
        }
        throw new ProductNotFoundException(id);
    }

    public Product finish(Long id) {
        Product product = productRepository.getById(id);
        if (product.getStatus() == Status.IN_PROGRESS) {
            product.setStatus(Status.COMPLETED);
            product.setActualEnd(LocalDateTime.now());
            product.setActualDuration(calculateWorkTimeLength(product.getActualStart(), product.getActualEnd()));
            updateLastModified(product);
            return productRepository.save(product);
        }
        throw new ProductNotFoundException(id);
    }

    public Product cancel(Long id) {
        Product product = productRepository.getById(id);
        if (product.getStatus() == Status.IN_PROGRESS) {
            product.setStatus(Status.CANCELED);
            updateLastModified(product);
            return productRepository.save(product);
        }
        throw new ProductNotFoundException(id);
    }

    public Product undoLastAction(Long id) {
        Product product = productRepository.getById(id);
        if (product.getStatus() != Status.WAITING) {
            product.setStatus(product.getStatus().previous());
            updateLastModified(product);
            return productRepository.save(product);
        }
        throw new ProductNotFoundException(id);
    }
}
