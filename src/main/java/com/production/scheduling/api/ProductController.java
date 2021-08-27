package com.production.scheduling.api;

import com.production.scheduling.exceptions.ProductNotFoundException;
import com.production.scheduling.logic.ProductionLogic;
import com.production.scheduling.model.PlannedProductionTime;
import com.production.scheduling.model.Product;
import com.production.scheduling.model.Status;
import com.production.scheduling.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/tasks/")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PostMapping("/products/new")
    public Product newProduct(@RequestBody Product product) {
        return productRepository.save(ProductionLogic.createNewProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @PutMapping(value = "/products/move/{id}")
    public Product moveProduct(@PathVariable Long id, @RequestBody PlannedProductionTime dates) {
        Product product = productRepository.getById(id);
        return productRepository.save(ProductionLogic.updateProductTimeSpan(dates, product));
    }

    @PutMapping("/products/start/{id}")
    public Product startProduction(@PathVariable Long id) {
        Product product = productRepository.getById(id);
        if (product.getStatus() == Status.WAITING) {
            return productRepository.save(ProductionLogic.start(product));
        }
        throw new ProductNotFoundException(id);
    }

    @PutMapping("/products/finish/{id}")
    public Product finishProduction(@PathVariable Long id) {
        Product product = productRepository.getById(id);
        if (product.getStatus() == Status.IN_PROGRESS) {
            return productRepository.save(ProductionLogic.finish(product));
        }
        throw new ProductNotFoundException(id);
    }

    @PutMapping("/products/cancel/{id}")
    public Product cancelProduction(@PathVariable Long id) {
        Product product = productRepository.getById(id);
        if (product.getStatus() == Status.IN_PROGRESS) {
            return productRepository.save(ProductionLogic.cancel(product));
        }
        throw new ProductNotFoundException(id);
    }

    @PutMapping("/products/undo/{id}")
    public Product undoLastAction(@PathVariable Long id) {
        Product product = productRepository.getById(id);
        if (product.getStatus() != Status.WAITING) {
            return productRepository.save(ProductionLogic.undoLastAction(product));
        }
        throw new ProductNotFoundException(id);
    }
}
