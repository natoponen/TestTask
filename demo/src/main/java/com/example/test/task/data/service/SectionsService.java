package com.example.test.task.data.service;

import com.example.test.task.data.model.Sections;

import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SectionsService {

    List<Sections> findAllSections();

    Sections findById(long id);

    Sections insert(Sections sections);

    boolean delete(long id);

    boolean update(Sections sections);

    List<Sections> containGeoClassByCode(String code);

    CompletableFuture<OutputStream> exportToXls();

    String importExportProgress(Long id);
}
