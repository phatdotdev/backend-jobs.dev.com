package com.dev.job.service;

import com.dev.job.dto.request.Posting.CreateJobPostingRequest;
import com.dev.job.dto.response.Posting.JobPostingResponse;
import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.user.Recruiter;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.mapper.PostingMapper;
import com.dev.job.repository.Posting.JobPostingRepository;
import com.dev.job.repository.User.RecruiterRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostingService {

    JobPostingRepository jobPostingRepository;
    RecruiterRepository recruiterRepository;
    PostingMapper postingMapper;

    public List<JobPostingResponse> getAllJobPostings (UUID rId){
        return jobPostingRepository
                .findByRecruiterId(rId)
                .stream()
                .map(postingMapper::toJobResponse)
                .toList();
    }

    public JobPostingResponse createJobPosting (CreateJobPostingRequest request, UUID rId){
        JobPosting posting = postingMapper.toJobPosting(request);
        Recruiter recruiter = recruiterRepository.findById(rId)
                .orElseThrow(() -> new BadRequestException("No recruiter with id: "+rId.toString()));
        posting.setRecruiter(recruiter);
        posting.setCreatedAt(LocalDate.now());
        posting.setUpdatedAt(LocalDate.now());
        return postingMapper.toJobResponse(
            jobPostingRepository.save(
                posting
            )
        );
    }
}
