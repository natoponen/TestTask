package com.example.test.task.data.service;

import com.example.test.task.data.model.GeologicalClass;
import com.example.test.task.data.model.Sections;
import com.example.test.task.data.reposity.SectionsRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class SectionService implements SectionsService{

    private final SectionsRepository sectionsRepository;

    public SectionService(SectionsRepository sectionsRepository) {
        this.sectionsRepository = sectionsRepository;
    }

    @Override
    public List<Sections> findAllSections() {
        return sectionsRepository.findAll();
    }

    @Override
    public Sections findById(long id) {
        Optional<Sections> res = sectionsRepository.findById(id);
        return res.orElse(null);
    }

    @Override
    public Sections insert(Sections sections) {
        return sectionsRepository.save(sections);
    }

    @Override
    public boolean delete(long id) {
        try {
            sectionsRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Sections sections) {
        try {
            sectionsRepository.save(sections);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Sections> containGeoClassByCode(String code) {
        List<Sections> result = new ArrayList<>();
        for (Sections sections : findAllSections()) {
            for (GeologicalClass gc: sections.getGeologicalClasses()) {
                if (gc.getCode().equals(code)) {
                    result.add(sections);
                }
            }
        }
        if (result.size()==0) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    @Async
    public CompletableFuture<OutputStream> exportToXls() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Creating header row
        Row headerRow = sheet.createRow(1);
        Cell headerSectionCell = headerRow.createCell(1);
        headerSectionCell.setCellValue("Section name");
        int headerColumnCount = 1;
        int maxNumberOfGeoClasses = findById(1L).getGeologicalClasses().size();
        for (Sections sections : findAllSections()) {
            if (sections.getGeologicalClasses().size()>maxNumberOfGeoClasses) {
                maxNumberOfGeoClasses = sections.getGeologicalClasses().size();
            }
        }
        for (int i=0; i<maxNumberOfGeoClasses; i++) {
            Cell headerGeoClassName = headerRow.createCell(++headerColumnCount);
            headerGeoClassName.setCellValue("Class "+i+1+" name");
            Cell headerGeoClassCode = headerRow.createCell(++headerColumnCount);
            headerGeoClassCode.setCellValue("Class "+i+1+" code");
        }

        int rowCount = 1;

        // Filling in the data
        for (Sections sections : findAllSections()) {
            Row row = sheet.createRow(++rowCount);
            Cell sectionNameCell = row.createCell(1);
            sectionNameCell.setCellValue(sections.getName());
            int columnCount = 1;
            for (GeologicalClass geologicalClass: sections.getGeologicalClasses()) {
                Cell geoClassNameCell = row.createCell(++columnCount);
                geoClassNameCell.setCellValue(geologicalClass.getName());
                Cell geoClassCodeCell = row.createCell(++columnCount);
                geoClassCodeCell.setCellValue(geologicalClass.getCode());
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream("Sections.xls")) {
            workbook.write(outputStream);
            return CompletableFuture.completedFuture(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String importExportProgress(Long id) {
        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        for (Thread thread: threads) {
            if (id.equals(thread.getId())) {
                Thread.State threadState = thread.getState();
                switch (threadState) {
                    case TERMINATED:
                        return "DONE";
                    case RUNNABLE:
                        return "IN PROGRESS";
                    default:
                        return "ERROR";
                }
            }
        }
        return "No job with such ID";
    }
}
