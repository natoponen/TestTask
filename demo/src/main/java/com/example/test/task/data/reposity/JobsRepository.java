package com.example.test.task.data.reposity;

import com.example.test.task.data.model.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobsRepository extends JpaRepository<JobEntity, Long> {
}
