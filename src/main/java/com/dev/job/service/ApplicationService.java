package com.dev.job.service;

import com.dev.job.dto.request.Application.UpdateApplicationStateRequest;
import com.dev.job.dto.response.Application.ApplicationResponse;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.dto.response.Resume.ResumeResponse;
import com.dev.job.entity.application.Application;
import com.dev.job.entity.application.ApplicationState;
import com.dev.job.entity.communication.Notification;
import com.dev.job.entity.communication.NotificationType;
import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.posting.PostState;
import com.dev.job.entity.resource.Document;
import com.dev.job.entity.resume.Resume;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.exceptions.ResourceNotFoundException;
import com.dev.job.exceptions.UnauthorizedException;
import com.dev.job.repository.Application.ApplicationRepository;
import com.dev.job.repository.Posting.JobPostingRepository;
import com.dev.job.repository.Resume.ResumeRepository;
import com.dev.job.repository.User.JobSeekerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Data
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationService {
    ApplicationRepository applicationRepository;
    JobSeekerRepository jobSeekerRepository;
    ResumeRepository resumeRepository;
    JobPostingRepository jobPostingRepository;

    ResumeService resumeService;
    PostingService postingService;
    NotificationService notificationService;
    UploadService uploadService;

    public Page<ApplicationResponse> getAllApplication(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return applicationRepository.findAll(pageable).map(this::toApplicationResponse);
    }

    @Transactional
    public ApplicationResponse applyJob(UUID postId, UUID resumeId, List<MultipartFile> files, UUID jsId) throws IOException {
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Resume resume = getResume(resumeId);
        if(applicationRepository.existsByJobPostingIdAndResumeId(postId, resumeId)){
            throw new BadRequestException("Can not create application.");
        }
        if(!resume.getJobSeeker().getId().equals(jobSeeker.getId())){
            throw new UnauthorizedException("You do not have permission with this resume");
        }
        JobPosting post = getJobPosting(postId);
        if(!post.getState().equals(PostState.PUBLISHED)){
            throw new BadRequestException("Post is not published.");
        }
        Application application = Application.builder()
                .state(ApplicationState.SUBMITTED)
                .resume(resume)
                .jobPosting(post)
                .appliedAt(LocalDateTime.now())
                .build();
        applicationRepository.save(application);
        List<Document> documents = uploadService.uploadDocuments(files, "documents/applications" , application.getId());
        application.setDocuments(documents);
        applicationRepository.save(application);
        return toApplicationResponse(application);
    }

    public ApplicationResponse searchApply(UUID postId, UUID jsId){
        return applicationRepository.findByJobPostingIdAndResume_JobSeeker_Id(postId, jsId)
                .map(this::toApplicationResponse)
                .orElse(null);
    }

    public Page<ApplicationResponse> getMyApplications(UUID jsId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return applicationRepository.findByResume_JobSeeker_Id(jsId, pageable)
                .map(this::toApplicationResponse);
    }

    public ApplicationResponse getApplicationById(UUID id, UUID jsId){
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found."));
        if(!application.getResume().getJobSeeker().getId().equals(jsId)){
            throw new UnauthorizedException("You do not have permission.");
        }
        return toApplicationResponse(application);
    }

    public Page<ApplicationResponse> getJobPostingApplication(UUID postId, ApplicationState state, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Application> applications;

        if (state != null) {
            applications = applicationRepository.findByJobPostingIdAndState(postId, state, pageable);
        } else {
            applications = applicationRepository.findByJobPostingId(postId, pageable);
        }

        return applications.map(this::toApplicationResponse);
    }

    public ApplicationResponse updateApplicationState(UUID appId, UpdateApplicationStateRequest request, UUID userId ){
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new BadRequestException("Application not found."));
        if(!application.getJobPosting().getRecruiter().getId().equals(userId)){
            throw new UnauthorizedException("You do not have permission.");
        }
//        if(application.getState().equals(ApplicationState.ACCEPTED) || application.getState().equals(ApplicationState.REJECTED)){
//            throw new BadRequestException("Can not update application state.");
//        }
        if(request.getState().equals(ApplicationState.SUBMITTED)){
            throw new BadRequestException("Can not update application state.");
        }
        application.setState(request.getState());
        applicationRepository.save(application);

        notificationService.sendUserNotification(
            Notification.builder()
                .recipientId(application.getResume().getJobSeeker().getId())
                .title("Trạng thái đơn ứng tuyển của bạn đã được cập nhật thành: " + request.getState().name())
                .application(application)
                .type(NotificationType.APPLICATION_STATUS_CHANGED)
                .content(request.getContent())
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build(), userId
        );

        return toApplicationResponse(application);
    }

    @Transactional
    public ApplicationResponse updateDocuments(UUID id, List<MultipartFile> files, UUID userId) throws IOException {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found."));
        if(!application.getResume().getJobSeeker().getId().equals(userId)){
            throw  new UnauthorizedException("You do not have permission");
        }
        List<Document> documents = uploadService.uploadDocuments(files, "documents/applications", id);
        application.getDocuments().addAll(documents);
        application.setState(ApplicationState.SUBMITTED);
        applicationRepository.save(application);
        return toApplicationResponse(application);
    }

    // PRIVATE METHOD

    private JobSeeker getJobSeeker(UUID jsId){
        return jobSeekerRepository.findById(jsId)
                .orElseThrow(() -> new BadRequestException("Job seeker not found."));
    }

    private Resume getResume(UUID resumeId){
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Not resume with id: "+resumeId.toString()));
    }

    private JobPosting getJobPosting(UUID postId){
        return jobPostingRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));
    }

    private ApplicationResponse toApplicationResponse(Application application) {
        ResumeResponse resumeResponse = resumeService.getResume(application.getResume().getId());
        JobPostingResponse postingResponse = postingService.getJobPosting(application.getJobPosting().getId());
        return ApplicationResponse.builder()
                .id(application.getId())
                .resume(resumeResponse)
                .post(postingResponse)
                .notifications(application.getNotifications())
                .documents(application.getDocuments())
                .state(application.getState())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .build();
    }
}
