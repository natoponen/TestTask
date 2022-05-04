package com.example.test.task.apis.controllers;

import com.example.test.task.data.services.SectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/export")
public class ExportController {
    private final SectionService service;

    public ExportController(SectionService service) {
        this.service = service;
    }

    @GetMapping("")
    public Long startExportToXls() {
        CompletableFuture<OutputStream> cf = service.exportToXls();

        return 0L;
    }

    @GetMapping("/{id}")
    public String exportProgress(@PathVariable("id") Long id) {
        return service.importExportProgress(id);
    }

    @GetMapping("/{id}/file")
    public void exportFileById(Long id) {
        service.exportToXls();
    }
}
