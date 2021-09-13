package com.production.scheduling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.production.scheduling.dto.PlannedProductionTime;
import com.production.scheduling.dto.ScheduleItem;
import com.production.scheduling.dto.Status;
import com.production.scheduling.exceptions.ProductNotFoundException;
import com.production.scheduling.service.ProductionService;
import com.production.scheduling.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
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

    @Test
    void searchingForAllProductsReturnProperProductArray() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Mockito.when(productionService
                        .findAll())
                .thenReturn(ResponseEntity.ok(products));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/product/tasks/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].description", is("Test packing node")));
    }

    @Test
    void searchingWithExistingIdReturnsProduct() throws Exception {
        Mockito.when(productionService
                        .findById(1L))
                .thenReturn(ResponseEntity.ok(product1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/product/tasks/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void creatingNewProductReturnsSuccess() throws Exception {
        ScheduleItem item = new ScheduleItem(LocalDateTime.now(), "Test product", 1L, 1L);

        product1.setDescription(item.getDescription());

        Mockito.when(productionService.createNewProduct(item))
                .thenReturn(ResponseEntity.ok(product1));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/product/tasks/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(item));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.description", is("Test product")));
    }

    @Test
    void deleteProductReturnsSuccess() throws Exception {
        Mockito.when(productionService
                        .findById(product1.getId()))
                .thenReturn(ResponseEntity.ok(product1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/product/tasks/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void moveProductReturnsSuccess() throws Exception {
        PlannedProductionTime time = new PlannedProductionTime(LocalDateTime.now(), LocalDateTime.now().plusMinutes(100));

        Mockito.when(productionService
                        .updateProductTimeSpan(time, product1.getId()))
                .thenReturn(ResponseEntity.ok(product1));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/product/tasks/products/move/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(time));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void startProductionReturnsSuccess() throws Exception {
        Mockito.when(productionService
                        .start(product1.getId()))
                .thenReturn(ResponseEntity.ok(product1));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/product/tasks/products/start/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void finishWaitingProductionReturnsNotFound() throws Exception {
        Mockito.doThrow(ProductNotFoundException.class)
                .when(productionService)
                .finish(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/product/tasks/products/finish/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelProductionReturnsSuccess() throws Exception {
        product1.setStatus(Status.IN_PROGRESS);

        Mockito.when(productionService
                        .cancel(product1.getId()))
                .thenReturn(ResponseEntity.ok(product1));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/product/tasks/products/cancel/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void undoLastActionReturnsSuccess() throws Exception {
        product1.setStatus(Status.COMPLETED);

        Mockito.when(productionService
                        .undoLastAction(product1.getId()))
                .thenReturn(ResponseEntity.ok(product1));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/product/tasks/products/undo/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}