package com.pramod.jobportal.controller;

import com.pramod.jobportal.Enum.Role;
import com.pramod.jobportal.exception.AccessDeniedException;
import com.pramod.jobportal.exception.ResourceNotFoundException;
import com.pramod.jobportal.model.Job;
import com.pramod.jobportal.model.User;
import com.pramod.jobportal.repository.JobRepository;
import com.pramod.jobportal.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Controller", description = "Job related APIs")
public class JobController {

    private final JobRepository jobRepository;

    @Autowired
    private final UserRepository userRepository;

    // ================= GET ALL JOBS =================
    @Operation(summary = "Get all jobs", description = "Job seeker and recruiter can view all jobs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jobs fetched successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'RECRUITER')")
    @GetMapping
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // ================= CREATE JOB =================
    @Operation(summary = "Create a Job", description = "Recruiter creates a new job")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Job created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public Job createJob(@RequestBody Job job) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        job.setRecruiter(recruiter);
        return jobRepository.save(job);
    }

    // ================= UPDATE JOB =================
    @Operation(summary = "Update Job", description = "Recruiter updates own job, Admin can update any job")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Job updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id,
                         @RequestBody Job updatedJob) {

        User currentUser = getLoggedInUser();

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (currentUser.getRole() == Role.RECRUITER &&
                !job.getRecruiter().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this job");
        }

        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setLocation(updatedJob.getLocation());
        job.setSalary(updatedJob.getSalary());

        return jobRepository.save(job);
    }

    // ================= DELETE JOB =================
    @Operation(summary = "Delete Job", description = "Recruiter deletes own job, Admin can delete any job")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Job deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteJob(@PathVariable Long id) {

        User currentUser = getLoggedInUser();

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (currentUser.getRole() == Role.RECRUITER &&
                !job.getRecruiter().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this job");
        }

        jobRepository.delete(job);
        return "Job deleted successfully";
    }

    // ================= GET JOB BY ID =================
    @Operation(summary = "Get Job by ID", description = "View job details by job ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Job fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @PreAuthorize("hasAnyRole('JOB_SEEKER','RECRUITER')")
    @GetMapping("/getJobById/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found with id: " + id));
    }

    // ================= SEARCH JOB =================
    @Operation(summary = "Search Jobs", description = "Search jobs by title and location")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jobs fetched successfully")
    })
    @PreAuthorize("hasAnyRole('JOB_SEEKER','RECRUITER')")
    @GetMapping("/search")
    public List<Job> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location
    ) {
        return jobRepository.searchJobs(title, location);
    }

    // ================= PAGINATION =================
    @Operation(summary = "Get Jobs with Pagination & Sorting")
    @PreAuthorize("hasAnyRole('JOB_SEEKER','RECRUITER','ADMIN')")
    @GetMapping("/paged")
    public Page<Job> getJobsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return jobRepository.findAll(pageable);
    }

    // ================= SEARCH + PAGINATION =================
    @Operation(summary = "Search Jobs with Pagination & Sorting")
    @PreAuthorize("hasAnyRole('JOB_SEEKER','RECRUITER','ADMIN')")
    @GetMapping("/search/paged")
    public Page<Job> searchJobsPaged(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return jobRepository
                .findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
                        title, location, pageable);
    }

    // ================= HELPER =================
    private User getLoggedInUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Logged-in user not found"));
    }
}
