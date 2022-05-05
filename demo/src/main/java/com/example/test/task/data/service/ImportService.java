package com.example.test.task.data.service;

import com.example.test.task.data.model.entity.JobEntity;
import com.example.test.task.data.model.entity.SectionEntity;
import com.example.test.task.data.reposity.JobsRepository;
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
import java.time.LocalDateTime;
import java.util.Iterator;

@Service
public class ImportService implements ImportingService{

    private final SectionsRepository sectionsRepository;
    private final JobsRepository jobsRepository;

    public ImportService(SectionsRepository sectionsRepository, JobsRepository jobsRepository) {
        this.sectionsRepository = sectionsRepository;
        this.jobsRepository = jobsRepository;
    }

    @Override
    public Long startImport(File file) {
        JobEntity job = new JobEntity();
        job.setType("import");
        job.setFile(null);
        jobsRepository.save(job);
        Long id = job.getId();
        importFromXsl(file, id);
        return id;
    }

    @Override
    @Async
    public void importFromXsl(File file, Long id) {
        JobEntity currentJob = jobsRepository.getById(id);
        currentJob.setStatus("IN PROGRESS");
        currentJob.setStart(LocalDateTime.now());
        jobsRepository.save(currentJob);
        HSSFWorkbook workbook;
        try (InputStream inputStream = new FileInputStream(file)) {
            workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            it.next(); //Skip headers row

            while (it.hasNext()) {
                SectionEntity anotherSections = new SectionEntity();

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
            currentJob.setStatus("ERROR");
            jobsRepository.save(currentJob);
            e.printStackTrace();
        }
        currentJob.setStatus("DONE");
        currentJob.setEnd(LocalDateTime.now());
        jobsRepository.save(currentJob);
    }
}
