package com.example.test.task.data.services;

import com.example.test.task.data.models.Section;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SectionsService {

    List<Section> findAllSections();

    Section findById(long id);

    Section insert(Section section);

    boolean delete(long id);

    boolean update(Section section);

    List<Section> containGeoClassByCode(String code);

    CompletableFuture<OutputStream> exportToXls();

    String importExportProgress(Long id);
}
