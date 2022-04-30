package com.example.test.task.data.services;

import com.example.test.task.data.models.GeologicalClass;
import com.example.test.task.data.models.Section;
import com.example.test.task.data.repositories.SectionRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    public CompletableFuture<Long> importFromXsl(File file) {
        HSSFWorkbook workbook;
        try (InputStream inputStream = new FileInputStream(file)) {
            workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            it.next(); //Skip headers row

            while (it.hasNext()) {
                Section anotherSection = new Section();

                Row row = it.next();
                Iterator<Cell> cells = row.iterator();

                anotherSection.setName(cells.next().getStringCellValue());

                while (cells.hasNext()) {
                    Cell geoClassName = cells.next();
                    Cell geoClassCode = cells.next();
                    anotherSection.addGeoClass(geoClassName.getStringCellValue(),
                            geoClassCode.getStringCellValue());

                }
                sectionRepository.save(anotherSection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(Thread.currentThread().getId());
    }

    @Override
    public String importingProgress (Long id) {
        
        return "PROGRESS";
    }

    @Override
    @Async
    public CompletableFuture<Long> exportToXsl() {
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
            return CompletableFuture.completedFuture(Thread.currentThread().getId());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String exportingProgress(Long id) {
        return "PROGRESS";
    }
}
