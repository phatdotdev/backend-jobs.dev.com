package com.dev.job.service;

import com.dev.job.entity.statistics.AdminStatisticsResponse;
import com.dev.job.entity.user.UserStatus;
import com.dev.job.repository.Application.ApplicationRepository;
import com.dev.job.repository.Posting.JobPostingRepository;
import com.dev.job.repository.User.RecruiterRepository;
import com.dev.job.repository.User.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StatisticsService {

    UserRepository userRepository;
    JobPostingRepository postRepository;
    ApplicationRepository applicationRepository;
    RecruiterRepository companyRepository;

    public AdminStatisticsResponse getStatistics() {
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
}
