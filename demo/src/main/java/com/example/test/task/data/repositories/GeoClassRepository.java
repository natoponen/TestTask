package com.example.test.task.data.repositories;

import com.example.test.task.data.models.GeologicalClass;
import com.example.test.task.data.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoClassRepository extends JpaRepository<GeologicalClass, Long> {}
