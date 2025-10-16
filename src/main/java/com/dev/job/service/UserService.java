package com.dev.job.service;

import com.dev.job.dto.request.User.UpdateJobSeekerRequest;
import com.dev.job.dto.request.User.UpdateRecruiterRequest;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.dev.job.dto.response.User.RecruiterResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.mapper.UserMapper;
import com.dev.job.repository.User.JobSeekerRepository;
import com.dev.job.repository.User.RecruiterRepository;
import com.dev.job.repository.User.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;
    JobSeekerRepository jobSeekerRepository;
    RecruiterRepository recruiterRepository;
    UserMapper userMapper;

    public List<UserResponse> getAllUsers(){
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public Page<UserResponse> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    public Page<UserResponse> searchUsers(String search, Pageable pageable){
        return userRepository.findByUsernameContainingIgnoreCase(search, pageable).map(userMapper::toResponse);
    }

    public JobSeekerResponse getJobSeekerProfile(UUID id){
        return userMapper.jobSeekerToResponse(
            jobSeekerRepository.findById(id).orElseThrow(
                () -> new BadRequestException("Job seeker not found.")
            )
        );
    }

    public JobSeekerResponse updateJobSeekerProfile(UpdateJobSeekerRequest request, UUID id){
        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Job seeker not found."));
        userMapper.updateRequestToJobSeeker(jobSeeker, request);
        jobSeeker.setUpdatedAt(LocalDateTime.now());
        jobSeeker.setUpdatedBy(userRepository.findById(id).orElse(null));
        return userMapper.jobSeekerToResponse(jobSeekerRepository.save(jobSeeker));
    }

    public RecruiterResponse getRecruiterProfile(UUID id){
        return userMapper.recruiterToResponse(
            recruiterRepository
                .findById(id)
                .orElseThrow(() -> new BadRequestException("Recruiter not found."))
        );
    }

    public RecruiterResponse updateRecruiterProfile(UpdateRecruiterRequest request, UUID id){
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Recruiter not found."));
        userMapper.updateRequestToRecruiter(recruiter, request);
        recruiter.setUpdatedAt(LocalDateTime.now());
        return userMapper.recruiterToResponse(recruiterRepository.save(recruiter));
    }

}
