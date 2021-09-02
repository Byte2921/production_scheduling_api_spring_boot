package com.production.scheduling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.production.scheduling.logic.ProductionLogic;
import com.production.scheduling.model.Operation;
import com.production.scheduling.model.Product;
import com.production.scheduling.model.ScheduleItem;
import com.production.scheduling.model.Workplace;
import com.production.scheduling.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ProductionLogic productionLogic;

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

        Mockito.when(productRepository
                        .findAll())
                .thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/product/tasks/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].description", is("Test packing node")));
    }

    @Test
    void searchingWithExistingIdReturnsProduct() throws Exception {
        Mockito.when(productRepository
                        .findById(1L))
                .thenReturn(Optional.of(product1));

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

        Mockito.when(productRepository
                        .save(productionLogic.createNewProduct(item)))
                .thenReturn(product1);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/product/tasks/products/new")
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
        Mockito.when(productRepository
                        .findById(product1.getId()))
                .thenReturn(Optional.of(product1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/product/tasks/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void moveProduct() {
    }

    @Test
    void startProduction() {
    }

    @Test
    void finishProduction() {
    }

    @Test
    void cancelProduction() {
    }

    @Test
    void undoLastAction() {
    }
}