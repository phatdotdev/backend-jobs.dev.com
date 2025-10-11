package com.dev.job.service;

import com.dev.job.dto.request.Resume.*;
import com.dev.job.dto.response.Resume.*;
import com.dev.job.entity.resume.*;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.mapper.ResumeMapper;
import com.dev.job.repository.Resume.*;
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
    ExperienceRepository experienceRepository;
    CertificationRepository certificationRepository;
    SkillRepository skillRepository;
    AwardRepository awardRepository;
    ActivityRepository activityRepository;
    ProjectRepository projectRepository;
    ResumeRepository resumeRepository;

    JobSeekerRepository jobSeekerRepository;
    ResumeMapper resumeMapper;

    /*********** EDUCATION SERVICE **********/

    public List<EducationResponse> getAllEducations(UUID jsId) {
        return educationRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toEducationResponse)
                .toList();
    }

    public EducationResponse addEducation(CreateEducationRequest request, UUID jsId){
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Education education = resumeMapper.toEducation(request);
        education.setJobSeeker(jobSeeker);
        return resumeMapper.toEducationResponse(educationRepository.save(education));
    }

    /*********** EXPERIENCE SERVICE **********/

    public List<ExperienceResponse> getAllExperiences(UUID jsId){
        return experienceRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toExperienceResponse)
                .toList();
    }

    public ExperienceResponse addExperience(CreateExperienceRequest request, UUID jsId){
        JobSeeker jobSeeker = getJobSeeker(jsId);
        WorkExperience experience = resumeMapper.toExperience(request);
        experience.setJobSeeker(jobSeeker);
        return resumeMapper.toExperienceResponse(experienceRepository.save(experience));
    }

    /*********** SKILL SERVICE **********/

    public List<SkillResponse> getAllSkills(UUID jsId) {
        return skillRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toSkillResponse)
                .toList();
    }

    public SkillResponse addSkill(CreateSkillRequest request, UUID jsId) {
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Skill skill = resumeMapper.toSkill(request);
        skill.setJobSeeker(jobSeeker);
        return resumeMapper.toSkillResponse(skillRepository.save(skill));
    }

    /*********** ACTIVITY SERVICE **********/

    public List<ActivityResponse> getAllActivities(UUID jsId) {
        return activityRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toActivityResponse)
                .toList();
    }

    public ActivityResponse addActivity(CreateActivityRequest request, UUID jsId) {
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Activity activity = resumeMapper.toActivity(request);
        activity.setJobSeeker(jobSeeker);
        return resumeMapper.toActivityResponse(activityRepository.save(activity));
    }

    /*********** AWARD SERVICE **********/

    public List<AwardResponse> getAllAwards(UUID jsId) {
        return awardRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toAwardResponse)
                .toList();
    }

    public AwardResponse addAward(CreateAwardRequest request, UUID jsId) {
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Award award = resumeMapper.toAward(request);
        award.setJobSeeker(jobSeeker);
        return resumeMapper.toAwardResponse(awardRepository.save(award));
    }

    /*********** PROJECT SERVICE **********/

    public List<ProjectResponse> getAllProjects(UUID jsId) {
        return projectRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toProjectResponse)
                .toList();
    }

    public ProjectResponse addProject(CreateProjectRequest request, UUID jsId) {
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Project project = resumeMapper.toProject(request);
        project.setJobSeeker(jobSeeker);
        return resumeMapper.toProjectResponse(projectRepository.save(project));
    }

    /*********** CERTIFICATION SERVICE **********/
    public List<CertificationResponse> getAllCertifications(UUID jsId) {
        return certificationRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toCertificationResponse)
                .toList();
    }

    public CertificationResponse addCertification(CreateCertificationRequest request, UUID jsId) {
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Certification certification = resumeMapper.toCertification(request);
        certification.setJobSeeker(jobSeeker);
        return resumeMapper.toCertificationResponse(certificationRepository.save(certification));
    }


    /*********** RESUME SERVICE **********/

    public List<ResumeResponse> getResume(UUID jsId) {
        return resumeRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(resumeMapper::toResumeResponse)
                .toList();
    }

    public ResumeResponse createOrUpdateResume(CreateResumeRequest request, UUID jsId) {
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Resume resume = resumeMapper.toResume(request);
        resume.setJobSeeker(jobSeeker);
        return resumeMapper.toResumeResponse(resumeRepository.save(resume));
    }


    /*********** PRIVATE METHOD **********/

    private JobSeeker getJobSeeker(UUID jsId){
        return jobSeekerRepository.findById(jsId)
                .orElseThrow(() -> new BadRequestException("Job seeker not found with id: "+jsId));
    }
}
