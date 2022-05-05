package com.example.test.task.data.model.convertor;

import com.example.test.task.data.model.dto.GeologicalClassDto;
import com.example.test.task.data.model.dto.SectionDto;
import com.example.test.task.data.model.entity.GeologicalClass;
import com.example.test.task.data.model.entity.SectionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SectionConvertor {
    private final ModelMapper modelMapper;

    public SectionConvertor() {
        this.modelMapper = new ModelMapper();
    }

    public SectionDto convertToDto(SectionEntity entity) {
        modelMapper.createTypeMap(GeologicalClass.class, GeologicalClassDto.class);
        return modelMapper.map(entity, SectionDto.class);
    }

    public SectionEntity convertToEntity(SectionDto dto) {
        modelMapper.createTypeMap(GeologicalClassDto.class, GeologicalClass.class);
        return modelMapper.map(dto, SectionEntity.class);
    }
}
