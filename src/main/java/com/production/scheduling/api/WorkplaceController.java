package com.production.scheduling.api;

import com.production.scheduling.exceptions.WorkplaceNotFoundException;
import com.production.scheduling.model.Workplace;
import com.production.scheduling.repository.WorkplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workplace/tasks/")
public class WorkplaceController {

    @Autowired
    public WorkplaceRepository workplaceRepository;

    @GetMapping("/workplaces")
    public List<Workplace> getAllWorkplaces() {
        return workplaceRepository.findAll();
    }

    @GetMapping("/workplaces/{id}")
    public Workplace getWorkplaceById(@PathVariable Long id) {
        return workplaceRepository.findById(id)
                .orElseThrow(() -> new WorkplaceNotFoundException(id));
    }

    @PostMapping("/workplaces/new")
    public Workplace createNewWorkplace(@RequestBody Workplace workplace) {
        return workplaceRepository.save(workplace);
    }

    @DeleteMapping("/workplaces/{id}")
    public void deleteWorkplace(@PathVariable Long id) {
        workplaceRepository.deleteById(id);
    }
}
