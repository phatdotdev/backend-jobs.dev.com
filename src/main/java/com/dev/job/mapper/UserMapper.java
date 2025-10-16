package com.dev.job.mapper;

import com.dev.job.dto.request.User.CreateUserRequest;
import com.dev.job.dto.request.User.UpdateJobSeekerRequest;
import com.dev.job.dto.request.User.UpdateRecruiterRequest;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.dev.job.dto.response.User.RecruiterResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.entity.user.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserResponse toResponse(User user);

    JobSeeker createRequestToJobSeeker(CreateUserRequest request);

    Recruiter createRequestToRecruiter(CreateUserRequest request);

    UserResponse userToResponse(JobSeeker user);
    UserResponse userToResponse(Recruiter user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToJobSeeker(@MappingTarget JobSeeker jobSeeker, UpdateJobSeekerRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRequestToRecruiter(@MappingTarget Recruiter recruiter, UpdateRecruiterRequest request);

    JobSeekerResponse jobSeekerToResponse(JobSeeker jobSeeker);
    RecruiterResponse recruiterToResponse(Recruiter recruiter);
}
