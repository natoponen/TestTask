package com.example.test.task.data.model.dto;

import com.example.test.task.data.model.entity.GeologicalClass;
import lombok.Data;

import java.util.List;

@Data
public class SectionDto {
    private String name;
    private List<GeologicalClass> geologicalClasses;
}