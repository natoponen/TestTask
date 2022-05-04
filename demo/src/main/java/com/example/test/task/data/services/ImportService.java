package com.example.test.task.data.services;

import com.example.test.task.data.models.Section;
import com.example.test.task.data.repositories.SectionRepository;
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

    private final SectionRepository sectionRepository;

    public ImportService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
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
}
