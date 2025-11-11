package com.dev.job.service;

import com.dev.job.dto.request.review.CreateFeedbackRequest;
import com.dev.job.dto.request.review.CreateFeedbackReview;
import com.dev.job.dto.response.review.FeedbackRequestResponse;
import com.dev.job.dto.response.review.FeedbackReviewResponse;
import com.dev.job.entity.resume.Resume;
import com.dev.job.entity.review.FeedbackRequest;
import com.dev.job.entity.review.FeedbackReview;
import com.dev.job.entity.review.FeedbackStatus;
import com.dev.job.entity.user.Expert;
import com.dev.job.exceptions.ResourceNotFoundException;
import com.dev.job.exceptions.UnauthorizedException;
import com.dev.job.repository.Resume.ResumeRepository;
import com.dev.job.repository.Review.FeedbackRequestRepository;
import com.dev.job.repository.Review.FeedbackReviewRepository;
import com.dev.job.repository.User.ExpertRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewService {

    ResumeService resumeService;

    ExpertRepository expertRepository;
    ResumeRepository resumeRepository;
    FeedbackRequestRepository feedbackRequestRepository;
    FeedbackReviewRepository feedbackReviewRepository;

    // CREATE REQUEST FOR RESUME
    public FeedbackRequestResponse createRequest(CreateFeedbackRequest dto, UUID userId) {
        Resume resume = resumeRepository.findById(dto.resumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found."));
        if(!resume.getJobSeeker().getId().equals(userId)){
            throw new UnauthorizedException("You do not have permission.");
        }
        FeedbackRequest request = FeedbackRequest.builder()
                .resume(resume)
                .status(FeedbackStatus.REVIEW)
                .notes(dto.notes())
                .createdAt(LocalDateTime.now())
                .completedAt(dto.completedAt())
                .build();
        feedbackRequestRepository.save(request);
        return mapToRequestResponse(request);
    }

    // GET ALL REQUEST STATE REVIEW
    public Page<FeedbackRequestResponse> getActiveRequests(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Page<FeedbackRequest> requests = feedbackRequestRepository.findByCompletedAtAfter(now, pageable);
        return requests.map(this::mapToRequestResponse);
    }


    // GET ALL REQUESTS BY RESUME
    public List<FeedbackRequestResponse> getRequestsByResume(UUID resumeId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow();
        List<FeedbackRequest> requests = feedbackRequestRepository.findByResume_Id(resumeId);
        return requests.stream().map(this::mapToRequestResponse).toList();
    }

    // CREATE REVIEW FOR REQUEST
    public FeedbackReviewResponse submitReview(UUID requestId, UUID expertId, CreateFeedbackReview dto) {
        FeedbackRequest request = feedbackRequestRepository.findById(requestId).orElseThrow(
                () -> new ResourceNotFoundException("Request not found.")
        );
        Expert expert = expertRepository.findById(expertId).orElseThrow();

        if (dto.score() < 0 || dto.score() > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100.");
        }

        FeedbackReview review = FeedbackReview.builder()
                .feedbackRequest(request)
                .expert(expert)
                .score(dto.score())
                .overallComment(dto.overallComment())
                .experienceComment(dto.experienceComment())
                .skillsComment(dto.skillsComment())
                .educationComment(dto.educationComment())
                .recommendation(dto.recommendation())
                .createdAt(LocalDateTime.now())
                .build();
        feedbackReviewRepository.save(review);

        markRequestAsDone(request);

        return mapToReviewResponse(review);
    }

    public FeedbackRequestResponse getRequestById(UUID id) {
        FeedbackRequest request = feedbackRequestRepository.findById(id).orElseThrow();
        return mapToRequestResponse(request);
    }

    public Page<FeedbackReviewResponse> getMyFeedbacks(UUID expertId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FeedbackReview> reviewPage = feedbackReviewRepository.findByExpert_Id(expertId, pageable);

        return reviewPage.map(this::mapToReviewResponse);
    }

    public FeedbackReviewResponse getFeedbackById(UUID id, UUID userId){
        FeedbackReview review = feedbackReviewRepository.findById(id).orElseThrow();
        if(!review.getExpert().getId().equals(userId)){
            throw new UnauthorizedException("You do not hava permission.");
        }
        return mapToReviewResponse(review);
    }


    // PRIVATE METHOD

    private void markRequestAsDone(FeedbackRequest request) {
        request.setStatus(FeedbackStatus.DONE);
        request.setCompletedAt(LocalDateTime.now());
        feedbackRequestRepository.save(request);
    }


    private FeedbackRequestResponse mapToRequestResponse(FeedbackRequest request) {
        List<FeedbackReviewResponse> reviews = request.getReviews().stream()
                .map(this::mapToReviewResponse)
                .toList();

        return FeedbackRequestResponse.builder()
                .id(request.getId())
                .resume(resumeService.getResume(request.getResume().getId()))
                .notes(request.getNotes())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .completedAt(request.getCompletedAt())
                .reviews(reviews)
                .build();
    }

    private FeedbackReviewResponse mapToReviewResponse(FeedbackReview review) {
        return FeedbackReviewResponse.builder()
                .id(review.getId())
                .feedbackRequestId(review.getFeedbackRequest().getId())
                .expertId(review.getExpert().getId())
                .score(review.getScore())
                .overallComment(review.getOverallComment())
                .experienceComment(review.getExperienceComment())
                .skillsComment(review.getSkillsComment())
                .educationComment(review.getEducationComment())
                .recommendation(review.getRecommendation())
                .resume(resumeService.getResume(review.getFeedbackRequest().getResume().getId()))
                .createdAt(review.getCreatedAt())
                .build();
    }

}
