package com.example.test.task.data.service;

import com.example.test.task.data.model.dto.SectionDto;
import com.example.test.task.data.model.entity.SectionEntity;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SectionsService {

    List<SectionDto> findAllSections();

    SectionDto findById(long id);

    void insert(SectionDto sections);

    boolean delete(long id);

    boolean update(SectionDto sections);

    List<SectionDto> containGeoClassByCode(String code);

    Long startExport();

    void exportToXls(Long id);

    String importExportProgress(Long id);

    File getExportFileById(Long id) throws Exception;
}
