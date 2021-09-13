package com.production.scheduling.repository;

import com.production.scheduling.dto.Status;
import com.production.scheduling.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndStatus(Long id, Status status);

    Optional<Product> findByIdAndStatusNotIn(Long id, Collection<Status> statuses);
}
