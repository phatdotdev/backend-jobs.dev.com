package com.dev.job.service;


import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.dto.response.recommendation.CompanyRecResponse;
import com.dev.job.dto.response.recommendation.RecServiceResponse;
import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.repository.Posting.JobPostingRepository;
import com.dev.job.repository.User.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final JobPostingRepository jobPostingRepository;
    private final PostingService postingService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final RecruiterRepository recruiterRepository;
    @Value("${recommendation.service.url}")
    private String recServiceUrl;

    public List<JobPostingResponse> getRecommendedJobs(UUID userId) {
        String apiUrl = recServiceUrl + "/api/v1/recommendations/";

        System.out.println(apiUrl);
        System.out.println(userId);


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", userId.toString());
        requestBody.put("limit", 10);

        try {
            RecServiceResponse response = restTemplate.postForObject(
                    apiUrl, requestBody, RecServiceResponse.class
            );

            if (response == null || response.getRecommendations().isEmpty()) {
                return new ArrayList<>();
            }

            List<UUID> jobIds = response.getRecommendations().stream()
                    .map(rec -> UUID.fromString(rec.getJobId()))
                    .toList();

            List<JobPosting> jobs = jobPostingRepository.findAllById(jobIds);

            Map<UUID, JobPosting> jobMap = jobs.stream()
                    .collect(Collectors.toMap(JobPosting::getId, job -> job));

            List<JobPostingResponse> result = new ArrayList<>();
            for (UUID id : jobIds) {
                if (jobMap.containsKey(id)) {
                    result.add(postingService.toJobResponse(jobMap.get(id)));
                }
            }
            return result;

        } catch (Exception e) {
            System.err.println("Lỗi gọi Rec Service: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Recruiter> getRecommendedCompanies(UUID userId) {
        String apiUrl = recServiceUrl + "/api/v1/recommendations/companies";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("user_id", userId.toString());
        requestBody.put("limit", 8);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {

            CompanyRecResponse response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, CompanyRecResponse.class
            ).getBody();

            if (response == null || response.getRecommendations() == null || response.getRecommendations().isEmpty()) {
                return new ArrayList<>();
            }

            Map<UUID, Double> scoreMap = new HashMap<>();
            List<UUID> companyIds = new ArrayList<>();

            for (CompanyRecResponse.CompanyRecItem item : response.getRecommendations()) {
                try {
                    UUID id = UUID.fromString(item.getCompanyId());
                    companyIds.add(id);
                    scoreMap.put(id, item.getScore());
                } catch (IllegalArgumentException e) {

                }
            }

            List<Recruiter> recruiters = recruiterRepository.findAllById(companyIds);

            recruiters.sort((r1, r2) -> {
                Double s1 = scoreMap.getOrDefault(r1.getId(), 0.0);
                Double s2 = scoreMap.getOrDefault(r2.getId(), 0.0);
                return s2.compareTo(s1);
            });

            return recruiters;

        } catch (Exception e) {
            System.err.println("Lỗi gọi Company Recommendation: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<JobPostingResponse> getSimilarJobs(UUID jobId) {
        String apiUrl = recServiceUrl + "/api/v1/recommendations/similar-jobs";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("job_id", jobId.toString());
        requestBody.put("limit", 6);

        try {
            // 1. Gọi FastAPI
            RecServiceResponse response = restTemplate.postForObject(
                    apiUrl, requestBody, RecServiceResponse.class
            );

            if (response == null || response.getRecommendations().isEmpty()) {
                return new ArrayList<>();
            }

            List<UUID> jobIds = response.getRecommendations().stream()
                    .map(rec -> UUID.fromString(rec.getJobId()))
                    .collect(Collectors.toList());

            List<JobPosting> jobs = jobPostingRepository.findAllById(jobIds);

            Map<UUID, JobPosting> jobMap = jobs.stream()
                    .collect(Collectors.toMap(JobPosting::getId, job -> job));

            List<JobPostingResponse> result = new ArrayList<>();
            for (UUID id : jobIds) {
                if (jobMap.containsKey(id)) {
                    result.add(postingService.toJobResponse(jobMap.get(id)));
                }
            }

            return result;

        } catch (Exception e) {
            System.err.println("Lỗi lấy Similar Jobs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}