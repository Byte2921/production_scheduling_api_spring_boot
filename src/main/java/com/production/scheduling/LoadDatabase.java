package com.production.scheduling;

import com.production.scheduling.model.Operation;
import com.production.scheduling.model.Product;
import com.production.scheduling.model.Status;
import com.production.scheduling.model.Workplace;
import com.production.scheduling.repository.OperationRepository;
import com.production.scheduling.repository.ProductRepository;
import com.production.scheduling.repository.WorkplaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.Month;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(OperationRepository operationRepository, WorkplaceRepository workplaceRepository, ProductRepository productRepository) {
        return args -> {
            Operation operation1 = new Operation("Building");
            Workplace workplace1 = new Workplace("Building machine");
            Product product1 = new Product(LocalDateTime.of(2021, Month.AUGUST, 1, 6, 0,0),
                    LocalDateTime.of(2021, Month.AUGUST, 1, 18, 0,0),
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
            Product product2 = new Product(LocalDateTime.of(2021, Month.AUGUST, 1, 18, 0,0),
                    LocalDateTime.of(2021, Month.AUGUST, 2, 6, 0,0),
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

            log.info("Preloading " + operationRepository.save(operation1));
            log.info("Preloading " + workplaceRepository.save(workplace1));
            log.info("Preloading" + productRepository.save(product1));

            log.info("Preloading " + operationRepository.save(operation2));
            log.info("Preloading " + workplaceRepository.save(workplace2));
            log.info("Preloading" + productRepository.save(product2));
        };
    }
}
