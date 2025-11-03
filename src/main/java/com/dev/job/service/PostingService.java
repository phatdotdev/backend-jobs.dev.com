package com.dev.job.service;

import com.dev.job.dto.request.Posting.CreateJobPostingRequest;
import com.dev.job.dto.request.Posting.UpdateJobPostingRequest;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.posting.JobType;
import com.dev.job.entity.posting.PostState;
import com.dev.job.entity.resource.Image;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.entity.user.User;
import com.dev.job.entity.user.UserRole;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.exceptions.ResourceNotFoundException;
import com.dev.job.exceptions.UnauthorizedException;
import com.dev.job.mapper.PostingMapper;
import com.dev.job.repository.Posting.JobPostingRepository;
import com.dev.job.repository.Resource.LocationRepository;
import com.dev.job.repository.User.RecruiterRepository;
import com.dev.job.repository.User.UserRepository;
import com.dev.job.specification.JobPostingSpecification;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostingService {

    JobPostingRepository jobPostingRepository;
    RecruiterRepository recruiterRepository;
    UserRepository userRepository;
    PostingMapper postingMapper;
    UploadService uploadService;
    LocationRepository locationRepository;

    @Transactional
    public JobPostingResponse createJobPosting(CreateJobPostingRequest request,
                                               List<MultipartFile> imageFiles,
                                               UUID recruiterId) throws IOException {
        JobPosting job = postingMapper.toJobPosting(request);
        job.setLocation(locationRepository.findById(request.getLocationId()).orElseThrow());
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new BadRequestException("No recruiter with id: " + recruiterId));
        job.setRecruiter(recruiter);
        job.setState(PostState.DRAFT);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        jobPostingRepository.save(job);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<Image> uploadedImages = uploadService.uploadJobImages(imageFiles, job.getId());
            job.setImages(uploadedImages);
        }

        jobPostingRepository.save(job);

        return toJobResponse(job);
    }

    public Page<JobPostingResponse> searchJobPostings(String keyword,
                                                      BigDecimal minSalary,
                                                      BigDecimal maxSalary,
                                                      UUID locationId,
                                                      JobType type,
                                                      int page,
                                                      int size) {
        Specification<JobPosting> spec = JobPostingSpecification.buildSpec(keyword, minSalary, maxSalary, locationId, type, PostState.PUBLISHED);
        Pageable pageable = PageRequest.of(page, size, Sort.by("expiredAt").descending());
        return jobPostingRepository.findAll(spec, pageable)
                .map(this::toJobResponse);
    }

    public Page<JobPostingResponse> getAllJobPostings(String keyword,
                                                      BigDecimal minSalary,
                                                      BigDecimal maxSalary,
                                                      UUID locationId,
                                                      JobType type,
                                                      int page,
                                                      int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "expiredAt").descending());
        Specification<JobPosting> spec = JobPostingSpecification.buildSpec(keyword, minSalary, maxSalary, locationId, type, null);

        return jobPostingRepository.findAll(spec, pageable).map(this::toJobResponse);
    }

    public Page<JobPostingResponse> getMineJobPostings(UUID rId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "expiredAt").descending());
        return jobPostingRepository.findByRecruiterId(rId, pageable)
                .map(this::toJobResponse);
    }

    public JobPostingResponse getJobPosting(UUID pId) {
        return postingMapper.toJobResponse(jobPostingRepository.findById(pId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found.")));
    }

    public JobPostingResponse updateJobPostingState(PostState state, UUID pId, UUID rId){
        JobPosting posting = jobPostingRepository.findById(pId)
                .orElseThrow(() -> new ResourceNotFoundException("No job posting with id: " + pId));

        if (!posting.getRecruiter().getId().equals(rId)) {
            throw new UnauthorizedException("You do not have permission.");
        }

        if (posting.getState() == PostState.ARCHIVED && state == PostState.PUBLISHED) {
            throw new IllegalStateException("Unable to move from storage to active.");
        }

        posting.setState(state);
        jobPostingRepository.save(posting);
        return toJobResponse(posting);
    }

    @Transactional
    public JobPostingResponse updateJobPosting(UpdateJobPostingRequest request,
                                               UUID pId,
                                               UUID rId,
                                               List<MultipartFile> imageFiles) throws IOException {
        JobPosting posting = jobPostingRepository.findById(pId)
                .orElseThrow(() -> new ResourceNotFoundException("No job posting with id: " + pId));

        if (!posting.getRecruiter().getId().equals(rId)) {
            throw new UnauthorizedException("You do not have permission.");
        }

        postingMapper.updateJobPostingFromRequest(posting, request);
        posting.setUpdatedAt(LocalDateTime.now());

        if (imageFiles != null && !imageFiles.isEmpty()) {
            uploadService.deleteImages(posting.getImages());
            List<Image> uploadedImages = uploadService.uploadJobImages(imageFiles, posting.getId());
            posting.setImages(uploadedImages);
        }

        jobPostingRepository.save(posting);

        return toJobResponse(posting);
    }

    @Transactional
    public void deleteJobPosting(UUID postId, UUID userId) {
        JobPosting post = jobPostingRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found."));

        boolean isOwner = post.getRecruiter().getId().equals(userId);
        boolean isAdmin = user.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("You do not have permission.");
        }

        uploadService.deletePostImageFolder(postId);

        jobPostingRepository.delete(post);
    }

    public JobPostingResponse toJobResponse(JobPosting job) {
        return JobPostingResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .type(job.getType())
                .companyName(job.getRecruiter().getCompanyName())
                .experience(job.getExperience())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .benefits(job.getBenefits())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .location(job.getLocation())
                .state(job.getState())
                .views(job.getViews())
                .likes(job.getLikes())
                .imageNames(
                        job.getImages() != null
                                ? job.getImages().stream().map(Image::getFileName).toList()
                                : List.of()
                )
                .createdAt(job.getCreatedAt())
                .expiredAt(job.getExpiredAt())
                .build();
    }

}
