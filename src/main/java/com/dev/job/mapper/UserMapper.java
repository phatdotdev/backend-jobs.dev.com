package com.dev.job.mapper;

import com.dev.job.dto.request.User.CreateUserRequest;
import com.dev.job.dto.response.UserResponse;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.entity.user.Recruiter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    JobSeeker createRequestToJobSeeker(CreateUserRequest request);
    Recruiter createRequestToRecruiter(CreateUserRequest request);
    UserResponse userToResponse(JobSeeker user);
    UserResponse userToResponse(Recruiter user);
}
