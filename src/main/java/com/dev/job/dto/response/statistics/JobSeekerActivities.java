package com.dev.job.dto.response.statistics;

import com.dev.job.dto.response.Posting.JobPostingResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerActivities {
    List<JobPostingResponse> views;
    List<JobPostingResponse> likes;
    List<JobPostingResponse> applies;
}
