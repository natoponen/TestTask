package com.example.test.task.apis.controllers;

import com.example.test.task.data.models.Section;
import com.example.test.task.data.services.SectionService;
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
    public List<Section> findAll() {
        return service.findAllSections();
    }

    @GetMapping("/{id}")
    public Section findById(@PathVariable("id") long id) {
        return service.findById(id);
    }

    @PostMapping("")
    public Section insert(@RequestBody Section section) {
        if (section != null) {
            return service.insert(section);
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
    public String update(@RequestBody Section section) {
        if (section != null) {
            if (service.update(section)) {
                return "Updated section "+section.getId();
            } else {
                return "Unable to update section "+section.getId();
            }
        } else {
            return "Request body is empty";
        }
    }

    @GetMapping("/by-code")
    public List<Section> sectionsByCode(@RequestParam(value = "code") String code) {
        return service.containGeoClassByCode(code);
    }
}
