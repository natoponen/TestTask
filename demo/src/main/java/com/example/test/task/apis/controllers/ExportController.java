package com.example.test.task.apis.controllers;

import com.example.test.task.data.services.SectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/export")
public class ExportController {
    private final SectionService service;

    public ExportController(SectionService service) {
        this.service = service;
    }

    @GetMapping("")
    public CompletableFuture<Long> exportToXls() {
        return service.exportToXsl();
    }

    @GetMapping("/{id}")
    public String exportingProgress(@PathVariable("id") long id) {
        return service.exportingProgress(id);
    }
}
