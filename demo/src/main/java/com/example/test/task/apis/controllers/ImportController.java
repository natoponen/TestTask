package com.example.test.task.apis.controllers;

import com.example.test.task.data.services.ImportService;
import com.example.test.task.data.services.SectionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final ImportService importService;
    private final SectionService sectionService;

    public ImportController(ImportService importService, SectionService sectionService) {
        this.importService = importService;
        this.sectionService = sectionService;
    }


    @PostMapping("")
    public Long importFromXls (@RequestBody File file) {
        importService.importFromXsl(file);
        return 0L;
    }

    @GetMapping("/{id}")
    public String importProgress(@PathVariable("id") Long id) {
        return sectionService.importExportProgress(id);
    }
}
