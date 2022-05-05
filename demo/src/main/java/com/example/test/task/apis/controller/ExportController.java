package com.example.test.task.apis.controller;

import com.example.test.task.data.service.SectionService;
import com.example.test.task.data.service.SectionsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/export")
public class ExportController {
    private final SectionsService service;

    public ExportController(SectionService service) {
        this.service = service;
    }

    @GetMapping("")
    public Long startExportToXls() {
        Long jobId = service.startExport();
        return jobId;
    }

    @GetMapping("/{id}")
    public String exportProgress(@PathVariable("id") Long id) {
        return service.importExportProgress(id);
    }

    @GetMapping("/{id}/file")
    public File exportFileById(@PathVariable("id") Long id) throws Exception {
        return service.getExportFileById(id);
    }
}
