package com.example.test.task.apis.controllers;

import com.example.test.task.data.services.SectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final SectionService service;

    public ImportController(SectionService service) {
        this.service = service;
    }

    @PostMapping("")
    public CompletableFuture<Long> importFromXls (@RequestBody File file) {
        return service.importFromXsl(file);
    }

    @GetMapping("/{id}")
    public String importingProgress(@PathVariable("id") long id) {
        return service.importingProgress(id);
    }
}
