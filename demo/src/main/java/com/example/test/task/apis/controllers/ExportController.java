package com.example.test.task.apis.controllers;

import com.example.test.task.data.services.SectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/export")
public class ExportController {
    private final SectionService service;

    public ExportController(SectionService service) {
        this.service = service;
    }

    @GetMapping("")
    public long exportToXls() {
        Future<Long> id = service.exportToXsl();
        try {
            return id.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @GetMapping("/{id}")
    public String exportingProgress(@PathVariable("id") long id) {
        return service.exportingProgress(id);
    }
}
