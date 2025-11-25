package com.dev.job.service;

import com.dev.job.dto.response.statistics.AdminStatisticsResponse;
import com.dev.job.dto.response.statistics.JobSeekerActivities;
import com.dev.job.dto.response.statistics.RecruiterStatisticsResponse;
import com.dev.job.entity.application.Application;
import com.dev.job.entity.application.ApplicationState;
import com.dev.job.entity.posting.PostState;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.entity.user.UserStatus;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.repository.Application.ApplicationRepository;
import com.dev.job.repository.Posting.JobPostingRepository;
import com.dev.job.repository.User.JobSeekerRepository;
import com.dev.job.repository.User.RecruiterRepository;
import com.dev.job.repository.User.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatisticsService {

    UserRepository userRepository;
    JobSeekerRepository jobSeekerRepository;
    JobPostingRepository postRepository;
    ApplicationRepository applicationRepository;
    RecruiterRepository companyRepository;
    JobPostingRepository jobPostingRepository;

    PostingService postingService;

    public AdminStatisticsResponse getAdminStatistics() {
        Long totalUsers = userRepository.count();
        Long totalPosts = postRepository.count();
        Long totalApplications = applicationRepository.count();
        Long totalCompanies = companyRepository.count();

        Long totalActiveAccounts = userRepository.countByStatus(UserStatus.ACTIVE);
        Long totalInactiveAccounts = userRepository.countByStatus(UserStatus.INACTIVE);
        Long totalBannedAccounts = userRepository.countByStatus(UserStatus.BANNED);

        LocalDateTime startOfThisWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime startOfLastWeek = startOfThisWeek.minusWeeks(1);
        LocalDateTime endOfLastWeek = startOfThisWeek.minusSeconds(1);

        Long usersLastWeek = userRepository.countByCreatedAtBetween(startOfLastWeek, endOfLastWeek);
        Long postsLastWeek = postRepository.countByCreatedAtBetween(startOfLastWeek, endOfLastWeek);
        Long applicationsLastWeek = applicationRepository.countByAppliedAtBetween(startOfLastWeek, endOfLastWeek);
        Long companiesLastWeek = companyRepository.countByCreatedAtBetween(startOfLastWeek, endOfLastWeek);

        return AdminStatisticsResponse.builder()
                .totalUsers(totalUsers)
                .totalPosts(totalPosts)
                .totalApplications(totalApplications)
                .totalCompanies(totalCompanies)
                .totalActiveAccounts(totalActiveAccounts)
                .totalInactiveAccounts(totalInactiveAccounts)
                .totalBannedAccounts(totalBannedAccounts)
                .usersChange(totalUsers - usersLastWeek)
                .postsChange(totalPosts - postsLastWeek)
                .applicationsChange(totalApplications - applicationsLastWeek)
                .companiesChange(totalCompanies - companiesLastWeek)
                .build();
    }

    public RecruiterStatisticsResponse getRecruiterStatistics(UUID recruiterId) {
        Long totalJobPosts = jobPostingRepository.countByRecruiterId(recruiterId);
        Long activeJobPosts = jobPostingRepository.countByRecruiterIdAndState(recruiterId, PostState.PUBLISHED);
        Long draftJobPosts = jobPostingRepository.countByRecruiterIdAndState(recruiterId, PostState.DRAFT);
        Long completedJobPosts = jobPostingRepository.countByRecruiterIdAndState(recruiterId, PostState.COMPLETED);

        LocalDateTime startOfThisWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime startOfLastWeek = startOfThisWeek.minusWeeks(1);
        LocalDateTime endOfLastWeek = startOfThisWeek.minusSeconds(1);

        Long totalApplications = applicationRepository.countByJobPostingRecruiterId(recruiterId);
        Long applicationsThisWeek = applicationRepository.countByJobPostingRecruiterIdAndAppliedAtBetween(recruiterId, startOfLastWeek, endOfLastWeek);

        Long rejectedApplications = applicationRepository.countByJobPostingRecruiterIdAndState(recruiterId, ApplicationState.REJECTED);
        Long hiresCompleted = applicationRepository.countByJobPostingRecruiterIdAndState(recruiterId, ApplicationState.HIRED);

        Double hiringRate;
        if(totalApplications == 0){
            hiringRate = 0.0;
        } else {
            hiringRate = hiresCompleted*1.0 / totalApplications;
        }

        return RecruiterStatisticsResponse.builder()
                .totalJobPosts(totalJobPosts)
                .completedJobPosts(completedJobPosts)
                .activeJobPosts(activeJobPosts)
                .draftJobPosts(draftJobPosts)
                .totalApplications(totalApplications)
                .applicationsThisWeek(applicationsThisWeek)
                .totalApplications(totalApplications)
                .hiresCompleted(hiresCompleted)
                .rejectedApplications(rejectedApplications)
                .hiringRate(hiringRate)
                .build();
    }

    public JobSeekerActivities getJobSeekerActivities(UUID id){
        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Not found job seeker"));

        List<Application> applicationList = applicationRepository.findByResume_JobSeeker_Id(id);

        return JobSeekerActivities.builder()
                .views(jobSeeker.getViews().stream().map(postingService::toJobResponse).toList())
                .likes(jobSeeker.getLikes().stream().map(postingService::toJobResponse).toList())
                .applies(applicationRepository.findJobPostingsByUserId(id).stream().map(postingService::toJobResponse).toList())
                .build();
    }

}
