package com.dev.job.entity.statistics;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminStatisticsResponse {
    Long totalUsers;
    Long totalPosts;
    Long totalApplications;
    Long totalCompanies;

    Long totalActiveAccounts;
    Long totalInactiveAccounts;
    Long totalBannedAccounts;

    Long usersChange;
    Long postsChange;
    Long applicationsChange;
    Long companiesChange;
}
