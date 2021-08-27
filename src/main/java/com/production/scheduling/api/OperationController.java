package com.production.scheduling.api;

import com.production.scheduling.exceptions.OperationNotFoundException;
import com.production.scheduling.exceptions.WorkplaceNotFoundException;
import com.production.scheduling.model.Operation;
import com.production.scheduling.model.Workplace;
import com.production.scheduling.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operation/tasks/")
public class OperationController {
    @Autowired
    public OperationRepository operationRepository;

    @GetMapping("/operation")
    public List<Operation> getAllWOperations() {
        return operationRepository.findAll();
    }

    @GetMapping("/operation/{id}")
    public Operation getOperationById(@PathVariable Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new OperationNotFoundException(id));
    }

    @PostMapping("/operation/new")
    public Operation createNewOperation(@RequestBody Operation operation) {
        return operationRepository.save(operation);
    }

    @DeleteMapping("/operation/{id}")
    public void deleteOperation(@PathVariable Long id) {
        operationRepository.deleteById(id);
    }
}
