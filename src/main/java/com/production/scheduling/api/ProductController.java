package com.production.scheduling.api;

import com.production.scheduling.exceptions.ProductNotFoundException;
import com.production.scheduling.logic.ProductionLogic;
import com.production.scheduling.model.PlannedProductionTime;
import com.production.scheduling.model.Product;
import com.production.scheduling.model.ScheduleItem;
import com.production.scheduling.model.Status;
import com.production.scheduling.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/tasks")
public class ProductController {

    @Autowired
    private ProductionLogic logic;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return logic.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return logic.findById(id);
    }

    @PostMapping("/products/new")
    public Product newProduct(@RequestBody ScheduleItem scheduleItem) {
        return logic.createNewProduct(scheduleItem);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        logic.deleteById(id);
    }

    @PutMapping(value = "/products/move/{id}")
    public Product moveProduct(@PathVariable Long id, @RequestBody PlannedProductionTime dates) {
        return logic.updateProductTimeSpan(dates, id);
    }

    @PutMapping("/products/start/{id}")
    public Product startProduction(@PathVariable Long id) {
        return logic.start(id);
    }

    @PutMapping("/products/finish/{id}")
    public Product finishProduction(@PathVariable Long id) {
        return logic.finish(id);
    }

    @PutMapping("/products/cancel/{id}")
    public Product cancelProduction(@PathVariable Long id) {
        return logic.cancel(id);
    }

    @PutMapping("/products/undo/{id}")
    public Product undoLastAction(@PathVariable Long id) {
        return logic.undoLastAction(id);
    }
}
