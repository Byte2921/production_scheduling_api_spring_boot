package com.production.scheduling.api;

import com.production.scheduling.exceptions.ProductNotFoundException;
import com.production.scheduling.logic.ProductionLogic;
import com.production.scheduling.model.PlannedProductionTime;
import com.production.scheduling.model.Product;
import com.production.scheduling.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class SchedulingController {
    @Autowired
    ProductRepository productRepository;

    private static final Logger log = LoggerFactory.getLogger(SchedulingController.class);

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        log.info("Requested all products");
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        log.info("Requested product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PostMapping("/products/new")
    public Product newProduct(@RequestBody Product product) {
        log.info("Created new product with parameters: {}", product);
        return productRepository.save(ProductionLogic.CreateNewProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        log.info("Deleting product by id: {}", id);
        productRepository.deleteById(id);
    }

    @PostMapping(value = "/products/move/{id}")
    public int moveProduct(@PathVariable Long id, @RequestBody PlannedProductionTime dates) {
        log.info("Moving product with id: {} to new scheduled date between {} and {}", id, dates.getPlanStart(), dates.getPlanEnd());
        return productRepository.moveProduct(dates.getPlanStart(), dates.getPlanEnd(),
                ProductionLogic.calculateWorkTimeLength(dates.getPlanStart(), dates.getPlanEnd()), LocalDateTime.now(), System.getProperty("user.name"), id);
    }
}
