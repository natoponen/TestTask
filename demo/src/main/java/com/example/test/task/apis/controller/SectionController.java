package com.example.test.task.apis.controller;

import com.example.test.task.data.model.Sections;
import com.example.test.task.data.service.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionService service;

    public SectionController(SectionService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<Sections> findAll() {
        return service.findAllSections();
    }

    @GetMapping("/{id}")
    public Sections findById(@PathVariable("id") long id) {
        return service.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Sections insert(@RequestBody Sections sections) {
        if (sections != null) {
            return service.insert(sections);
        } else {
            return null;
        }
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id) {
        if (id > 0) {
            if (service.delete(id)) {
                return "Deleted section "+id;
            } else {
                return "Cannot delete section "+id;
            }
        } else {
            return "Invalid id";
        }
    }

    @PutMapping("")
    public String update(@RequestBody Sections sections) {
        if (sections != null) {
            if (service.update(sections)) {
                return "Updated section "+ sections.getId();
            } else {
                return "Unable to update section "+ sections.getId();
            }
        } else {
            return "Request body is empty";
        }
    }

    @GetMapping("/by-code")
    public List<Sections> sectionsByCode(@RequestParam(value = "code") String code) {
        return service.containGeoClassByCode(code);
    }
}
