package com.dev.job.mapper;

import com.dev.job.dto.request.Resume.CreateEducationRequest;
import com.dev.job.dto.response.Resume.EducationResponse;
import com.dev.job.entity.resume.Education;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
    Education toEducation(CreateEducationRequest request);
    EducationResponse toEducationResponse(Education education);
}
