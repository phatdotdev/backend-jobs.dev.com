package com.dev.job.dto.response.statistics;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecruiterStatisticsResponse {
    Long totalJobPosts;
    Long activeJobPosts;
    Long completedJobPosts;
    Long draftJobPosts;

    Long totalApplications;
    Long applicationsThisWeek;

    Long rejectedApplications;
    Long hiresCompleted;

    Double hiringRate;
}
