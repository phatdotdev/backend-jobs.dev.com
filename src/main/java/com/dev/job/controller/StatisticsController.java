package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.dto.response.statistics.AdminStatisticsResponse;
import com.dev.job.dto.response.statistics.JobSeekerActivities;
import com.dev.job.dto.response.statistics.RecruiterStatisticsResponse;
import com.dev.job.service.StatisticsService;
import static com.dev.job.utils.ResponseHelper.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatisticsController {

    StatisticsService statisticsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<ApiResponse<AdminStatisticsResponse>> getAdminStatistics(){
        return ok(statisticsService.getAdminStatistics());
    }

    @GetMapping("/recruiter")
    ResponseEntity<ApiResponse<RecruiterStatisticsResponse>> getRecruiterStatistics(Authentication authentication){
        return ok(statisticsService.getRecruiterStatistics(UUID.fromString(authentication.getName())));
    }

    @GetMapping("/job-seeker")
    ResponseEntity<ApiResponse<JobSeekerActivities>> getJobSeekerActivities(Authentication authentication){
        return ok(statisticsService.getJobSeekerActivities(UUID.fromString(authentication.getName())));
    }

}
