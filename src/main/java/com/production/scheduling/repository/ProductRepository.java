package com.production.scheduling.repository;

import com.production.scheduling.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query(value = "UPDATE PRODUCTS P SET P.planStart = ?, P.planEnd = ?, P.planDuration = ?, P.modified = ?, P.modifiedBy = ? WHERE p.id = ?", nativeQuery = true)
    Product moveProduct(LocalDateTime planStart, LocalDateTime planEnd, Long planDuration, LocalDateTime modified, String modifiedBy, Long id);
}
