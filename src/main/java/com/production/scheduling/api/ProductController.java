package com.production.scheduling.api;

import com.production.scheduling.service.ProductionService;
import com.production.scheduling.dto.PlannedProductionTime;
import com.production.scheduling.model.Product;
import com.production.scheduling.dto.ScheduleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/product/tasks")
public class ProductController {

    @Autowired
    private ProductionService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return service.findAll();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> newProduct(@RequestBody ScheduleItem scheduleItem) {
        return service.createNewProduct(scheduleItem);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return service.deleteById(id);
    }

    @PutMapping("/products/move/{id}")
    public ResponseEntity<Product> moveProduct(@PathVariable Long id, @RequestBody PlannedProductionTime dates) {
        return service.updateProductTimeSpan(dates, id);
    }

    @PutMapping("/products/start/{id}")
    public ResponseEntity<Product> startProduction(@PathVariable Long id) {
        return service.start(id);
    }

    @PutMapping("/products/finish/{id}")
    public ResponseEntity<Product> finishProduction(@PathVariable Long id) {
        return service.finish(id);
    }

    @PutMapping("/products/cancel/{id}")
    public ResponseEntity<Product> cancelProduction(@PathVariable Long id) {
        return service.cancel(id);
    }

    @PutMapping("/products/undo/{id}")
    public ResponseEntity<Product> undoLastAction(@PathVariable Long id) {
        return service.undoLastAction(id);
    }
}
