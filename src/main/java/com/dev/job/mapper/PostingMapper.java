package com.dev.job.mapper;

import com.dev.job.dto.request.Posting.CreateJobPostingRequest;
import com.dev.job.dto.request.Posting.UpdateJobPostingRequest;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.entity.posting.JobPosting;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PostingMapper {
    JobPosting toJobPosting(CreateJobPostingRequest request);

    JobPostingResponse toJobResponse(JobPosting jobPosting);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateJobPostingFromRequest(@MappingTarget JobPosting entity, UpdateJobPostingRequest request);

}
