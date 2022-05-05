package com.example.test.task.apis.controller;

import com.example.test.task.data.model.dto.SectionDto;
import com.example.test.task.data.service.SectionService;
import com.example.test.task.data.service.SectionsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {

    private final SectionsService service;

    public SectionController(SectionService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<SectionDto> findAll() {
        return service.findAllSections();
    }

    @GetMapping("/{id}")
    public SectionDto findById(@PathVariable("id") long id) {
        return service.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public void insert(@RequestBody SectionDto sections) {
        service.insert(sections);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public boolean delete(@PathVariable("id") long id) {
        return service.delete(id);
    }

    @PutMapping("")
    public boolean update(@RequestBody SectionDto section) {
        return service.update(section);
    }

    @GetMapping("/by-code")
    public List<SectionDto> sectionsByCode(@RequestParam(value = "code") String code) {
        return service.containGeoClassByCode(code);
    }
}
