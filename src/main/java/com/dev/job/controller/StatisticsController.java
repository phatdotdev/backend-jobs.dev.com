package com.dev.job.controller;

import com.dev.job.dto.ApiResponse;
import com.dev.job.entity.statistics.AdminStatisticsResponse;
import com.dev.job.service.StatisticsService;
import static com.dev.job.utils.ResponseHelper.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatisticsController {

    StatisticsService statisticsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<ApiResponse<AdminStatisticsResponse>> getStatistics(){
        return ok(statisticsService.getStatistics());
    }

}
