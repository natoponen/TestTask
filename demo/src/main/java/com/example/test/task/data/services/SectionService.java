package com.example.test.task.data.services;

import com.example.test.task.data.models.GeologicalClass;
import com.example.test.task.data.models.Section;
import com.example.test.task.data.repositories.SectionRepository;
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

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<Section> findAllSections() {
        return sectionRepository.findAll();
    }

    @Override
    public Section findById(long id) {
        Optional<Section> res = sectionRepository.findById(id);
        return res.orElse(null);
    }

    @Override
    public Section insert(Section section) {
        return sectionRepository.save(section);
    }

    @Override
    public boolean delete(long id) {
        try {
            sectionRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Section section) {
        try {
            sectionRepository.save(section);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Section> containGeoClassByCode(String code) {
        List<Section> result = new ArrayList<>();
        for (Section section: findAllSections()) {
            for (GeologicalClass gc: section.getGeologicalClasses()) {
                if (gc.getCode().equals(code)) {
                    result.add(section);
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
        for (Section section: findAllSections()) {
            if (section.getGeologicalClasses().size()>maxNumberOfGeoClasses) {
                maxNumberOfGeoClasses = section.getGeologicalClasses().size();
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
        for (Section section: findAllSections()) {
            Row row = sheet.createRow(++rowCount);
            Cell sectionNameCell = row.createCell(1);
            sectionNameCell.setCellValue(section.getName());
            int columnCount = 1;
            for (GeologicalClass geologicalClass: section.getGeologicalClasses()) {
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
