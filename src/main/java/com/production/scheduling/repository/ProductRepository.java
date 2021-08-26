package com.production.scheduling.repository;

import com.production.scheduling.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE PRODUCTS P SET P.plan_Start = ?, P.plan_End = ?, P.plan_Duration = ?, P.modified = ?, P.modified_By = ? WHERE p.id = ?", nativeQuery = true)
    int moveProduct(LocalDateTime planStart, LocalDateTime planEnd, Long planDuration, LocalDateTime modified, String modifiedBy, Long id);
}
