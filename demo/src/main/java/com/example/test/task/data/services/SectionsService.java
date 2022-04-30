package com.example.test.task.data.services;

import com.example.test.task.data.models.Section;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface SectionsService {

    List<Section> findAllSections();

    Section findById(long id);

    Section insert(Section section);

    boolean delete(long id);

    boolean update(Section section);

    List<Section> containGeoClassByCode(String code);

    CompletableFuture<Long> importFromXsl(File file);

    String importingProgress(Long id);

    CompletableFuture<Long> exportToXsl();

    String exportingProgress(Long id);
}
