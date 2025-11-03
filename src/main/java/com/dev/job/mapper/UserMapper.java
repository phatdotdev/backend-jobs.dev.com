package com.dev.job.mapper;

import com.dev.job.dto.request.User.*;
import com.dev.job.dto.response.User.ExpertResponse;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.dev.job.dto.response.User.RecruiterResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.entity.user.Expert;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.entity.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /********** COMMON **********/
    UserResponse toResponse(User user);

    /********** CREATE **********/
    JobSeeker createRequestToJobSeeker(CreateUserRequest request);
    JobSeeker createRequestToJobSeeker(CreateJobSeekerRequest request);

    Recruiter createRequestToRecruiter(CreateUserRequest request);
    Recruiter createRequestToRecruiter(CreateRecruiterRequest request);

    Expert createRequestToExpert(CreateExpertRequest request);

    /********** UPDATE **********/
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToJobSeeker(@MappingTarget JobSeeker jobSeeker, UpdateJobSeekerRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToRecruiter(@MappingTarget Recruiter recruiter, UpdateRecruiterRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToExpert(@MappingTarget Expert expert, UpdateExpertRequest request);

    /********** RESPONSE **********/
    JobSeekerResponse jobSeekerToResponse(JobSeeker jobSeeker);
    RecruiterResponse recruiterToResponse(Recruiter recruiter);
    ExpertResponse expertToResponse(Expert expert);

    UserResponse userToResponse(JobSeeker user);
    UserResponse userToResponse(Recruiter user);
    UserResponse userToResponse(Expert user);
}
