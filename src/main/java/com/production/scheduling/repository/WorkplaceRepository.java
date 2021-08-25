package com.production.scheduling.repository;

import com.production.scheduling.model.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
}
