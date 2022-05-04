package com.example.test.task.data.service;

import com.example.test.task.data.model.Sections;
import com.example.test.task.data.reposity.SectionsRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

@Service
public class ImportService {

    private final SectionsRepository sectionsRepository;

    public ImportService(SectionsRepository sectionsRepository) {
        this.sectionsRepository = sectionsRepository;
    }

    @Async
    public CompletableFuture<Long> importFromXsl(File file) {
        HSSFWorkbook workbook;
        try (InputStream inputStream = new FileInputStream(file)) {
            workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            it.next(); //Skip headers row

            while (it.hasNext()) {
                Sections anotherSections = new Sections();

                Row row = it.next();
                Iterator<Cell> cells = row.iterator();

                anotherSections.setName(cells.next().getStringCellValue());

                while (cells.hasNext()) {
                    Cell geoClassName = cells.next();
                    Cell geoClassCode = cells.next();
                    anotherSections.addGeoClass(geoClassName.getStringCellValue(),
                            geoClassCode.getStringCellValue());
                }
                sectionsRepository.save(anotherSections);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(Thread.currentThread().getId());
    }
}
