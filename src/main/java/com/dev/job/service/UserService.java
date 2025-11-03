package com.dev.job.service;

import com.dev.job.dto.request.User.*;
import com.dev.job.dto.response.User.ExpertResponse;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.dev.job.dto.response.User.RecruiterResponse;
import com.dev.job.dto.response.User.UserResponse;
import com.dev.job.entity.resource.Image;
import com.dev.job.entity.user.*;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.mapper.UserMapper;
import com.dev.job.repository.User.ExpertRepository;
import com.dev.job.repository.User.JobSeekerRepository;
import com.dev.job.repository.User.RecruiterRepository;
import com.dev.job.repository.User.UserRepository;
import com.dev.job.specification.ExpertSpecification;
import com.dev.job.specification.JobSeekerSpecification;
import com.dev.job.specification.RecruiterSpecification;
import com.dev.job.specification.UserSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {
    UserRepository userRepository;
    JobSeekerRepository jobSeekerRepository;
    RecruiterRepository recruiterRepository;
    ExpertRepository expertRepository;
    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    /********** USER IMAGE **********/

    public Image getUserAvatar(UUID userId){
        User user = getUserById(userId);
        if(user.getAvatar() == null){
            throw new BadRequestException("User do not have avatar.");
        }
       return user.getAvatar();
    }

    public Image getUserBackground(UUID userId){
        User user = getUserById(userId);
        if(user.getCover() == null){
            throw new BadRequestException("User do not have background.");
        }
        return user.getCover();
    }

    public boolean updateUserStatus(UUID userId, UserStatus status) {
        return userRepository.findById(userId).map(user -> {
            user.setStatus(status);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    /************ USER MANAGEMENT ***********/

    public Page<UserResponse> getUsers(int page, int size, String username, String email, String role, String status) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (username != null && !username.isEmpty()) {
            spec = spec.and(UserSpecification.hasUsername(username));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and(UserSpecification.hasEmail(email));
        }
        if (role != null && !role.isEmpty()) {
            spec = spec.and(UserSpecification.hasRole(role));
        }

        if(status != null && !status.isEmpty()){
            spec = spec.and(UserSpecification.hasStatus(status));
        }

        Page<User> userPage = userRepository.findAll(spec, pageable);
        return userPage.map(userMapper::toResponse);
    }

    public UserResponse getUserInfo(UUID userId){
        return userMapper.toResponse(getUserById(userId));
    }

    public Page<UserResponse> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    public Page<UserResponse> searchUsers(String search, Pageable pageable){
        return userRepository.findByUsernameContainingIgnoreCase(search, pageable).map(userMapper::toResponse);
    }

    // JOBSEEKER MANAGEMENT

    public JobSeekerResponse createJobSeeker(CreateJobSeekerRequest request){
        JobSeeker jobSeeker = userMapper.createRequestToJobSeeker(request);
        jobSeeker.setRole(UserRole.JOBSEEKER);
        jobSeeker.setStatus(UserStatus.INACTIVE);
        jobSeeker.setPassword(passwordEncoder.encode(request.getPassword()));
        jobSeeker.setCreatedAt(LocalDateTime.now());
        return userMapper.jobSeekerToResponse(jobSeekerRepository.save(jobSeeker));
    }

    public Page<JobSeekerResponse> getJobSeekers(int page, int size, String username, String email, String phone ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<JobSeeker> spec = (root, query, cb) -> cb.conjunction();

        if (username != null && !username.isEmpty()) {
            spec = spec.and(JobSeekerSpecification.hasUsername(username));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and(JobSeekerSpecification.hasEmail(email));
        }

        if (phone != null && !phone.isEmpty()) {
            spec = spec.and(JobSeekerSpecification.hasPhone(phone));
        }

        Page<JobSeeker> jobSeekerPage = jobSeekerRepository.findAll(spec, pageable);
        return jobSeekerPage.map(userMapper::jobSeekerToResponse);

    }

    // RECRUITER MANAGEMENT

    public RecruiterResponse createRecruiter(CreateRecruiterRequest request){
        Recruiter recruiter = userMapper.createRequestToRecruiter(request);
        recruiter.setRole(UserRole.RECRUITER);
        recruiter.setStatus(UserStatus.INACTIVE);
        recruiter.setPassword(passwordEncoder.encode(request.getPassword()));
        recruiter.setCreatedAt(LocalDateTime.now());

        return userMapper.recruiterToResponse(recruiterRepository.save(recruiter));
    }

    public Page<RecruiterResponse> getRecruiters(int page, int size, String username, String email, String phone, String companyName) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<Recruiter> spec = ((root, query, cb) -> cb.conjunction());

        if (username != null && !username.isEmpty()) {
            spec = spec.and(RecruiterSpecification.hasUsername(username));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and(RecruiterSpecification.hasEmail(email));
        }
        if (phone != null && !phone.isEmpty()) {
            spec = spec.and(RecruiterSpecification.hasPhone(phone));
        }
        if (companyName != null && !companyName.isEmpty()) {
            spec = spec.and(RecruiterSpecification.hasCompanyName(companyName));
        }

        Page<Recruiter> recruiterPage = recruiterRepository.findAll(spec, pageable);
        return recruiterPage.map(userMapper::recruiterToResponse);
    }

    public RecruiterResponse getRecruiterById(UUID id){
        return recruiterRepository.findById(id)
                .map(userMapper::recruiterToResponse)
                .orElseThrow(() -> new BadRequestException("Recruiter not found."));
    }

    // EXPERT MANAGEMENT

    public ExpertResponse createExpert(CreateExpertRequest request){
        Expert expert = userMapper.createRequestToExpert(request);
        expert.setPassword(passwordEncoder.encode(request.getPassword()));
        expert.setStatus(UserStatus.INACTIVE);
        expert.setRole(UserRole.EXPERT);
        expert.setCreatedAt(LocalDateTime.now());
        return userMapper.expertToResponse(expertRepository.save(expert));
    }

    public Page<ExpertResponse> getExperts(int page, int size, String username, String email) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Specification<Expert> spec = ((root, query, cb) -> cb.conjunction());

        if (username != null && !username.isEmpty()) {
            spec = spec.and(ExpertSpecification.hasUsername(username));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and(ExpertSpecification.hasEmail(email));
        }

        Page<Expert> expertPage = expertRepository.findAll(spec, pageable);
        return expertPage.map(userMapper::expertToResponse);
    }

    // Delete User By ID

    public boolean deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        switch (user.getRole()) {
            case JOBSEEKER -> jobSeekerRepository.deleteById(userId);
            case RECRUITER -> recruiterRepository.deleteById(userId);
            case EXPERT -> expertRepository.deleteById(userId);
            default -> userRepository.deleteById(userId);
        }
        return true;
    }


    /*********** USER INFO **********/

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

    public ExpertResponse updateExpertProfile(UpdateExpertRequest request, UUID id) {
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Expert not found."));
        userMapper.updateRequestToExpert(expert, request);
        expert.setUpdatedAt(LocalDateTime.now());
        return userMapper.expertToResponse(expertRepository.save(expert));
    }


    /*********** PRIVATE METHOD **********/
    private User getUserById(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found."));
    }

}
