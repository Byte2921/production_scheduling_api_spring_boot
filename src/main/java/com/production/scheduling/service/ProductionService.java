package com.production.scheduling.service;

import com.production.scheduling.dto.PlannedProductionTime;
import com.production.scheduling.dto.ScheduleItem;
import com.production.scheduling.dto.Status;
import com.production.scheduling.exceptions.ProductNotFoundException;
import com.production.scheduling.model.*;
import com.production.scheduling.repository.OperationRepository;
import com.production.scheduling.repository.ProductRepository;
import com.production.scheduling.repository.WorkplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ProductionService {

    private static final int DIVIDER = 60;
    private static final long DEFAULT_WORKING_TIME = 100;
    private static final String URI_PATH = "/api/product/tasks";
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private WorkplaceRepository workplaceRepository;
    @Autowired
    private ProductRepository productRepository;

    public ProductionService() {
    }

    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    public ResponseEntity<Product> findById(Long id) {
        return ResponseEntity.ok(productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id)));
    }

    public Long calculateWorkTimeLength(LocalDateTime planStart, LocalDateTime planEnd) {
        long minutes = Duration.between(planStart, planEnd).getSeconds() / DIVIDER;
        return minutes <= 0 ? DEFAULT_WORKING_TIME : minutes;
    }

    public ResponseEntity<Product> createNewProduct(ScheduleItem scheduleItem) {
        try {
            Product product = new Product();
            product.setPlanStart(scheduleItem.getPlanStart());
            product.setPlanEnd(scheduleItem.getPlanStart().plusMinutes(DEFAULT_WORKING_TIME));
            product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
            product.setDescription(scheduleItem.getDescription());
            assignWorkplace(product, scheduleItem.getWorkplaceId());
            assignOperation(product, scheduleItem.getOperationId());
            signNewProduct(product);
            productRepository.save(product);
            return ResponseEntity.created(new URI(URI_PATH + "/products/" + product.getId())).body(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Void> deleteById(Long id) {
        try {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Product> updateProductTimeSpan(PlannedProductionTime dates, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        try {
            product.setPlanStart(dates.getPlanStart());
            product.setPlanEnd(dates.getPlanEnd());
            product.setPlanDuration(calculateWorkTimeLength(product.getPlanStart(), product.getPlanEnd()));
            updateLastModified(product);
            productRepository.save(product);
            return ResponseEntity.ok(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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

    public ResponseEntity<Product> start(Long id) {
        Product product = productRepository.findByIdAndStatus(id, Status.WAITING)
                .orElseThrow(() -> new ProductNotFoundException(id));
        try {
            product.setStatus(product.getStatus().next());
            product.setActualStart(LocalDateTime.now());
            updateLastModified(product);
            productRepository.save(product);
            return ResponseEntity.ok(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Product> finish(Long id) {
        Product product = productRepository.findByIdAndStatus(id, Status.IN_PROGRESS)
                .orElseThrow(() -> new ProductNotFoundException(id));
        try {
            product.setStatus(product.getStatus().next());
            product.setActualEnd(LocalDateTime.now());
            product.setActualDuration(calculateWorkTimeLength(product.getActualStart(), product.getActualEnd()));
            updateLastModified(product);
            productRepository.save(product);
            return ResponseEntity.ok(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Product> cancel(Long id) {
        Product product = productRepository.findByIdAndStatus(id, Status.IN_PROGRESS)
                .orElseThrow(() -> new ProductNotFoundException(id));
        try {
            product.setStatus(Status.CANCELED);
            updateLastModified(product);
            productRepository.save(product);
            return ResponseEntity.ok(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Product> undoLastAction(Long id) {
        Product product = productRepository.findByIdAndStatusNotIn(id, Arrays.asList(Status.CANCELED, Status.WAITING))
                .orElseThrow(() -> new ProductNotFoundException(id));
        try {
            product.setStatus(product.getStatus().previous());
            updateLastModified(product);
            productRepository.save(product);
            return ResponseEntity.ok(product);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
