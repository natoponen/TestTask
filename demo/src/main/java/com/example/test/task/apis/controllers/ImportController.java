package com.example.test.task.apis.controllers;

import com.example.test.task.data.services.SectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final SectionService service;

    public ImportController(SectionService service) {
        this.service = service;
    }

    @PostMapping("")
    public void importFromXls (@RequestBody File file) {
        service.importFromXsl(file);
    }

    @GetMapping("/{id}")
    public String importingProgress(@PathVariable("id") long id) {
        return service.importingProgress(id);
    }
}
