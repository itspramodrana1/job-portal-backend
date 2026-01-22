package com.pramod.jobportal.controller;

import com.pramod.jobportal.dto.ApplicantResponse;
import com.pramod.jobportal.exception.AlreadyAppliedException;
import com.pramod.jobportal.model.Job;
import com.pramod.jobportal.model.JobApplication;
import com.pramod.jobportal.model.User;
import com.pramod.jobportal.repository.JobApplicationRepository;
import com.pramod.jobportal.repository.JobRepository;
import com.pramod.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/apply/{jobId}")
    public String applyJob(@PathVariable Long jobId) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));


        // 🔥 NEW CHECK (IMPORTANT)
        boolean alreadyApplied =
                jobApplicationRepository.existsByUserIdAndJobId(user.getId(), jobId);

        if (alreadyApplied) {
            throw new AlreadyAppliedException("You have already applied for this job");
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);

        jobApplicationRepository.save(application);

        return "Job applied successfully";
    }



    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/job/{jobId}/applicants")
    public List<ApplicantResponse> getApplicantsForJob(@PathVariable Long jobId) {

        List<JobApplication> applications =
                jobApplicationRepository.findByJobId(jobId);
        return applications.stream()
                .map(app -> new ApplicantResponse(
                        app.getUser().getId(),
                        app.getUser().getName(),
                        app.getUser().getEmail()
                ))
                .toList();
    }


}
