package com.production.scheduling.service;

import com.production.scheduling.dto.PlannedProductionTime;
import com.production.scheduling.dto.ScheduleItem;
import com.production.scheduling.dto.Status;
import com.production.scheduling.exceptions.ProductNotFoundException;
import com.production.scheduling.model.*;
import com.production.scheduling.repository.OperationRepository;
import com.production.scheduling.repository.ProductRepository;
import com.production.scheduling.repository.WorkplaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductionLogicTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private WorkplaceRepository workplaceRepository;
    @Mock
    private OperationRepository operationRepository;
    @InjectMocks
    private ProductionService productionService;

    Operation operation1 = new Operation("Building");
    Workplace workplace1 = new Workplace("Building machine");
    Product product1 = new Product(LocalDateTime.of(2021, Month.AUGUST, 1, 6, 0, 0),
            LocalDateTime.of(2021, Month.AUGUST, 1, 18, 0, 0),
            720L,
            null,
            null,
            null,
            "Test building node",
            workplace1,
            operation1,
            LocalDateTime.now(),
            System.getProperty("user.name"),
            null,
            null);
    Operation operation2 = new Operation("Packing");
    Workplace workplace2 = new Workplace("Packing machine");
    Product product2 = new Product(LocalDateTime.of(2021, Month.AUGUST, 1, 18, 0, 0),
            LocalDateTime.of(2021, Month.AUGUST, 2, 6, 0, 0),
            720L,
            null,
            null,
            null,
            "Test packing node",
            workplace2,
            operation2,
            LocalDateTime.now(),
            System.getProperty("user.name"),
            null,
            null);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllReturnsEveryProduct() {
        List<Product> prodcuts = new ArrayList<>();
        prodcuts.add(product1);
        prodcuts.add(product2);
        Mockito.when(productRepository
                        .findAll())
                .thenReturn(prodcuts);
        assertEquals(prodcuts, productionService.findAll());
    }

    @Test
    void findByIdReturnsSameProduct() {
        Mockito.when(productRepository
                        .findById(1L))
                .thenReturn(Optional.of(product1));
        assertEquals(product1, productionService.findById(1L));
    }

    @Test
    void calculateWorkTimeLengthGivesProperMinutesBack() {
        LocalDateTime testTime = LocalDateTime.now();
        assertEquals(100L, productionService.calculateWorkTimeLength(testTime, testTime.plusMinutes(100)));
    }

    @Test
    void calculateWorkTimeLengthReturnsDefaultWorkingTimeWhenSpanIsNegativeOrZero() {
        LocalDateTime testTime = LocalDateTime.now();
        assertEquals(100L, productionService.calculateWorkTimeLength(testTime.plusMinutes(600), testTime));
    }

    @Test
    void createNewProductReturnsProperObject() throws URISyntaxException {
        ScheduleItem item = new ScheduleItem(LocalDateTime.now(), "Test product", 1L, 1L);
        Product testProduct = new Product();
        testProduct.setPlanStart(item.getPlanStart());
        testProduct.setPlanEnd(item.getPlanStart().plusMinutes(100));
        testProduct.setPlanDuration(100L);
        testProduct.setDescription(item.getDescription());
        testProduct.setWorkplace(workplace1);
        testProduct.setOperation(operation1);
        testProduct.setCreated(LocalDateTime.now());
        testProduct.setCreatedBy(System.getProperty("user.name"));

        Mockito.when(operationRepository
                        .getById(1L))
                .thenReturn(operation1);
        Mockito.when(workplaceRepository
                        .getById(1L))
                .thenReturn(workplace1);

        assertEquals(testProduct, productionService.createNewProduct(item).getBody());
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateProductTimeSpanSetsNewTimeData() {
        PlannedProductionTime time = new PlannedProductionTime(LocalDateTime.now(), LocalDateTime.now().plusMinutes(100));
        Product testProduct = product1;
        testProduct.setPlanStart(time.getPlanStart());
        testProduct.setPlanEnd(time.getPlanEnd());
        testProduct.setPlanDuration(100L);
        testProduct.setModified(LocalDateTime.now());
        testProduct.setModifiedBy(System.getProperty("user.name"));

        Mockito.when(productRepository
                        .getById(1L))
                .thenReturn(product1);

        Mockito.when(productRepository
                        .save(testProduct))
                .thenReturn(testProduct);

        assertEquals(testProduct, productionService.updateProductTimeSpan(time, 1L));
    }

    @Test
    void startChangesProductStatus() {
        Mockito.when(productRepository
                        .getById(1L))
                .thenReturn(product1);
        assertEquals(Status.IN_PROGRESS, productionService.start(1L).getStatus());
    }

    @Test
    void startThrowsExceptionWhenStatusIsNotWaiting() {
        product1.setStatus(Status.COMPLETED);

        Mockito.when(productRepository
                        .getById(1L))
                .thenReturn(product1);

        assertThrows(ProductNotFoundException.class, () -> {
            productionService.start(1L);
        });
    }

    @Test
    void finishChangesProductStatus() {
        product1.setStatus(Status.IN_PROGRESS);
        product1.setActualStart(LocalDateTime.now().minusMinutes(10));
        Mockito.when(productRepository
                        .getById(1L))
                .thenReturn(product1);

        assertEquals(Status.COMPLETED, productionService.finish(1L).getStatus());
    }

    @Test
    void cannotCancelWaitingProduct() {
        Mockito.when(productRepository
                        .getById(1L))
                .thenReturn(product1);

        assertThrows(ProductNotFoundException.class, () -> {
            productionService.cancel(1L);
        });
    }

    @Test
    void cannotUndoLastActionWhenStatusIsWaiting() {
        Mockito.when(productRepository
                        .getById(1L))
                .thenReturn(product1);

        assertThrows(ProductNotFoundException.class, () -> {
            productionService.undoLastAction(1L);
        });
    }
}