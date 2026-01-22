package com.pramod.jobportal.repository;

import com.pramod.jobportal.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    List<JobApplication> findByJobId(Long jobId);
}
