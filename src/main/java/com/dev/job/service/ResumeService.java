package com.dev.job.service;

import com.dev.job.dto.request.Resume.CreateEducationRequest;
import com.dev.job.dto.response.Resume.EducationResponse;
import com.dev.job.entity.resume.Education;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.mapper.ResumeMapper;
import com.dev.job.repository.Resume.EducationRepository;
import com.dev.job.repository.User.JobSeekerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ResumeService {

    EducationRepository educationRepository;
    JobSeekerRepository jobSeekerRepository;
    ResumeMapper resumeMapper;

    public List<EducationResponse> getAllEducations(UUID jsId) {
        return educationRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toEducationResponse)
                .toList();
    }

    public EducationResponse addEducation(CreateEducationRequest request, UUID jsId){
        JobSeeker jobSeeker = jobSeekerRepository.findById(jsId)
                .orElseThrow(() -> new BadRequestException("Not found job seeker."));
        Education education = resumeMapper.toEducation(request);
        education.setJobSeeker(jobSeeker);
        return resumeMapper.toEducationResponse(educationRepository.save(education));
    }
}
