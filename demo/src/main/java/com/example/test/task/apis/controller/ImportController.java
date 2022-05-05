package com.example.test.task.apis.controller;

import com.example.test.task.data.service.ImportService;
import com.example.test.task.data.service.ImportingService;
import com.example.test.task.data.service.SectionService;
import com.example.test.task.data.service.SectionsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final ImportingService importService;
    private final SectionsService sectionService;

    public ImportController(ImportService importService, SectionService sectionService) {
        this.importService = importService;
        this.sectionService = sectionService;
    }


    @PostMapping("")
    public Long importFromXls (@RequestBody File file) {
        Long jobId = importService.startImport(file);
        return jobId;
    }

    @GetMapping("/{id}")
    public String importProgress(@PathVariable("id") Long id) {
        return sectionService.importExportProgress(id);
    }
}
