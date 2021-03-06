package com.example.test.task.data.reposity;

import com.example.test.task.data.model.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionsRepository extends JpaRepository<SectionEntity, Long> {}