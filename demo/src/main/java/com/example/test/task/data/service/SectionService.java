package com.example.test.task.data.service;

import com.example.test.task.data.model.convertor.SectionConvertor;
import com.example.test.task.data.model.dto.SectionDto;
import com.example.test.task.data.model.entity.GeologicalClass;
import com.example.test.task.data.model.entity.JobEntity;
import com.example.test.task.data.model.entity.SectionEntity;
import com.example.test.task.data.reposity.JobsRepository;
import com.example.test.task.data.reposity.SectionsRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SectionService implements SectionsService{

    private final SectionsRepository sectionsRepository;
    private final JobsRepository jobsRepository;
    private final SectionConvertor sectionConvertor;
    private SessionFactory sessionFactory;

    public SectionService(SectionsRepository sectionsRepository, JobsRepository jobsRepository, SectionConvertor sectionConvertor) {
        this.sectionsRepository = sectionsRepository;
        this.jobsRepository = jobsRepository;
        this.sectionConvertor = sectionConvertor;
    }

    @Override
    public List<SectionDto> findAllSections() {
        return sectionsRepository.findAll()
                .stream()
                .map(sectionConvertor::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SectionDto findById(long id) {
        Optional<SectionEntity> res = sectionsRepository.findById(id);
        return sectionConvertor.convertToDto(res.orElse(null));
    }

    @Override
    public void insert(SectionDto dto) {
        SectionEntity section = sectionConvertor.convertToEntity(dto);
        if (section == null) {
            throw new RuntimeException();
        } else {
            sectionsRepository.save(section);
        }
    }

    @Override
    public boolean delete(long id) {
        if (id > 0) {
            try {
                sectionsRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else return false;
    }

    @Override
    public boolean update(SectionDto dto) {
        SectionEntity section = sectionConvertor.convertToEntity(dto);
        if (section != null) {
            try {
                sectionsRepository.save(section);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public List<SectionDto> containGeoClassByCode(String code) {

        Session session = sessionFactory.openSession();

        String queryString = "select sec from "+
                "SectionEntity sec join sec.geologicalClasses gc "+
                "where gc.code = :code";

        Query query = session.createQuery(queryString);
        query.setParameter("code", code);
        List<SectionEntity> res = query.list();
        session.close();

        if (res.size()==0) {
            return null;
        } else {
            return res.stream()
                    .map(sectionConvertor::convertToDto)
                    .collect(Collectors.toList());
        }

        /*List<SectionDto> result = new ArrayList<>();
        for (SectionEntity section : sectionsRepository.findAll()) {
            for (GeologicalClass gc: section.getGeologicalClasses()) {
                if (gc.getCode().equals(code)) {
                    result.add(sectionConvertor.convertToDto(section));
                }
            }
        }*/
    }

    @Override
    public Long startExport() {
        JobEntity job = new JobEntity();
        job.setType("export");
        jobsRepository.save(job);
        Long id = job.getId();
        exportToXls(id);
        return id;
    }

    @Override
    @Async
    public void exportToXls(Long id) {
        JobEntity currentJob = jobsRepository.getById(id);
        currentJob.setStatus("IN PROGRESS");
        currentJob.setStart(LocalDateTime.now());
        jobsRepository.save(currentJob);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        // Creating header row
        Row headerRow = sheet.createRow(1);
        Cell headerSectionCell = headerRow.createCell(1);
        headerSectionCell.setCellValue("Section name");
        int headerColumnCount = 1;
        int maxNumberOfGeoClasses = findById(1L).getGeologicalClasses().size();
        List<SectionEntity> sectionList = sectionsRepository.findAll();
        for (SectionEntity sections : sectionList) {
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
        for (SectionEntity sections : sectionList) {
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
        currentJob.setFile(new File("Sections.xls"));
        try (FileOutputStream outputStream = new FileOutputStream(currentJob.getFile())) {
            workbook.write(outputStream);
            currentJob.setStatus("DONE");
            currentJob.setEnd(LocalDateTime.now());
            jobsRepository.save(currentJob);
        } catch (IOException e) {
            currentJob.setStatus("ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public File getExportFileById(Long id) throws Exception{
        JobEntity job = jobsRepository.getById(id);
        if (job.getStatus().equals("IN PROGRESS")) {
            throw new Exception();
        } else if (job.getStatus().equals("ERROR")) {
            throw new Exception();
        } else {
            return job.getFile();
        }
    }

    @Override
    public String importExportProgress(Long id) {
        JobEntity job = jobsRepository.getById(id);
        return job.getStatus();
    }
}
