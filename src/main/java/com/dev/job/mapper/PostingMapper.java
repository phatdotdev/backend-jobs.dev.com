package com.dev.job.mapper;

import com.dev.job.dto.request.Posting.CreateJobPostingRequest;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.entity.posting.JobPosting;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostingMapper {
    JobPosting toJobPosting(CreateJobPostingRequest request);
    JobPostingResponse toJobResponse(JobPosting jobPosting);
}
