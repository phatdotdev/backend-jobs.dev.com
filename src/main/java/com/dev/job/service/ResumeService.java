package com.dev.job.service;

import com.dev.job.dto.request.Resume.*;
import com.dev.job.dto.response.Resume.*;
import com.dev.job.dto.response.User.JobSeekerResponse;
import com.dev.job.entity.resume.*;
import com.dev.job.entity.user.JobSeeker;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.exceptions.ResourceNotFoundException;
import com.dev.job.exceptions.UnauthorizedException;
import com.dev.job.mapper.ResumeMapper;
import com.dev.job.repository.Resume.*;
import com.dev.job.repository.User.JobSeekerRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    UserService userService;

    /*********** RESUME SERVICE **********/

    public List<ResumeResponse> getAllResumes(UUID jsId) {
        return resumeRepository
                .findByJobSeekerId(jsId)
                .stream()
                .map(this::resumeToResponse)
                .toList();
    }

    public ResumeResponse createResume(CreateResumeRequest request, UUID jsId) {
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Resume resume = Resume.builder()
                .title(request.getTitle())
                .introduction(request.getIntroduction())
                .objectCareer(request.getObjectCareer())
                .educations(educationRepository.findAllById(
                        request.getEducations() != null ? request.getEducations() : List.of()))
                .experiences(experienceRepository.findAllById(
                        request.getExperiences() != null ? request.getExperiences() : List.of()))
                .skills(skillRepository.findAllById(
                        request.getSkills() != null ? request.getSkills() : List.of()))
                .certifications(certificationRepository.findAllById(
                        request.getCertifications() != null ? request.getCertifications() : List.of()))
                .awards(awardRepository.findAllById(
                        request.getAwards() != null ? request.getAwards() : List.of()))
                .activities(activityRepository.findAllById(
                        request.getActivities() != null ? request.getActivities() : List.of()))
                .jobSeeker(jobSeeker)
                .createdAt(LocalDateTime.now())
                .build();

        return this.resumeToResponse(resumeRepository.save(resume));
    }

    public ResumeResponse getResume(UUID resumeId){
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new BadRequestException("Resume not found"));
        UUID ownerId = resume.getJobSeeker().getId();
        JobSeekerResponse owner = userService.getJobSeekerProfile(ownerId);
        ResumeResponse response = resumeToResponse(resume);
        addJobSeekerInfo(response, owner);
        return response;
    }

    @Transactional
    public ResumeResponse updateResume(UpdateResumeRequest request, UUID resumeId, UUID jsId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new BadRequestException("Resume not found"));

        if (!resume.getJobSeeker().getId().equals(jsId)) {
            throw new BadRequestException("Resume does not belong to this job seeker");
        }

        resume.setTitle(request.getTitle());
        resume.setIntroduction(request.getIntroduction());
        resume.setObjectCareer(request.getObjectCareer());
        resume.setEducations(educationRepository.findAllById(request.getEducations()));
        resume.setExperiences(experienceRepository.findAllById(request.getExperiences()));
        resume.setSkills(skillRepository.findAllById(request.getSkills()));
        resume.setAwards(awardRepository.findAllById(request.getAwards()));
        resume.setActivities(activityRepository.findAllById(request.getActivities()));
        resume.setCertifications(certificationRepository.findAllById(request.getCertifications()));
        resume.setUpdatedAt(LocalDateTime.now());

        return resumeToResponse(resumeRepository.save(resume));
    }

    public String deleteResume(UUID resumeId, UUID jsId){
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new BadRequestException("Resume not found"));
        if (!resume.getJobSeeker().getId().equals(jsId)) {
            throw new BadRequestException("Resume does not belong to this job seeker");
        }
        resumeRepository.delete(resume);
        return "Delete resume successfully.";
    }


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
        education.setCreatedAt(LocalDateTime.now());
        return resumeMapper.toEducationResponse(educationRepository.save(education));
    }

    public EducationResponse updateEducation(UpdateEducationRequest request, UUID eduId,UUID jsId){
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Education education = getEducation(eduId);
        if(!education.getJobSeeker().getId().equals(jsId)){
            throw new UnauthorizedException("You do not have permission.");
        }
        resumeMapper.updateEducation(education, request);
        educationRepository.save(education);
        education.setUpdatedAt(LocalDateTime.now());
        return resumeMapper.toEducationResponse(education);
    }

    public void deleteEducation(UUID eduId,UUID jsId){
        JobSeeker jobSeeker = getJobSeeker(jsId);
        Education education = getEducation(eduId);
        if(!education.getJobSeeker().getId().equals(jsId)){
            throw new UnauthorizedException("You do not have permission.");
        }
        educationRepository.deleteById(eduId);
    }

    private Education getEducation(UUID eduId){
        return educationRepository.findById(eduId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found."));
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
        experience.setCreatedAt(LocalDateTime.now());
        return resumeMapper.toExperienceResponse(experienceRepository.save(experience));
    }

    public ExperienceResponse updateExperience(UpdateExperienceRequest request, UUID expId, UUID jsId){
        JobSeeker jobSeeker = getJobSeeker(jsId);
        WorkExperience experience = getExperience(expId);
        if(!experience.getJobSeeker().getId().equals(jsId)){
            throw new UnauthorizedException("You do not have permission.");
        }
        resumeMapper.updateExperience(experience, request);
        experience.setUpdatedAt(LocalDateTime.now());
        return resumeMapper.toExperienceResponse(experienceRepository.save(experience));
    }

    public void deleteExperience(UUID expId, UUID jsId){
        JobSeeker jobSeeker = getJobSeeker(jsId);
        WorkExperience experience = getExperience(expId);
        if(!experience.getJobSeeker().getId().equals(jsId)){
            throw new UnauthorizedException("You do not have permission.");
        }
        experienceRepository.deleteById(expId);
    }

    private WorkExperience getExperience(UUID id){
        return experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found."));
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
        skill.setCreatedAt(LocalDateTime.now());
        return resumeMapper.toSkillResponse(skillRepository.save(skill));
    }

    public SkillResponse updateSkill(UpdateSkillRequest request, UUID skillId, UUID jsId) {
        Skill skill = getSkill(skillId);
        if (!skill.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        resumeMapper.updateSkill(skill, request);
        skill.setUpdatedAt(LocalDateTime.now());
        return resumeMapper.toSkillResponse(skillRepository.save(skill));
    }

    public void deleteSkill(UUID skillId, UUID jsId) {
        Skill skill = getSkill(skillId);
        if (!skill.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        skillRepository.deleteById(skillId);
    }

    private Skill getSkill(UUID skillId) {
        return skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found."));
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
        activity.setCreatedAt(LocalDateTime.now());
        return resumeMapper.toActivityResponse(activityRepository.save(activity));
    }

    public ActivityResponse updateActivity(UpdateActivityRequest request, UUID activityId, UUID jsId) {
        Activity activity = getActivity(activityId);
        if (!activity.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        resumeMapper.updateActivity(activity, request);
        activity.setUpdatedAt(LocalDateTime.now());
        return resumeMapper.toActivityResponse(activityRepository.save(activity));
    }

    public void deleteActivity(UUID activityId, UUID jsId) {
        Activity activity = getActivity(activityId);
        if (!activity.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        activityRepository.deleteById(activityId);
    }

    private Activity getActivity(UUID activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found."));
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
        award.setCreatedAt(LocalDateTime.now());
        return resumeMapper.toAwardResponse(awardRepository.save(award));
    }

    public AwardResponse updateAward(UpdateAwardRequest request, UUID awardId, UUID jsId) {
        Award award = getAward(awardId);
        if (!award.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        resumeMapper.updateAward(award, request);
        award.setUpdatedAt(LocalDateTime.now());
        return resumeMapper.toAwardResponse(awardRepository.save(award));
    }

    public void deleteAward(UUID awardId, UUID jsId) {
        Award award = getAward(awardId);
        if (!award.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        awardRepository.deleteById(awardId);
    }

    private Award getAward(UUID awardId) {
        return awardRepository.findById(awardId)
                .orElseThrow(() -> new ResourceNotFoundException("Award not found."));
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
        project.setCreatedAt(LocalDateTime.now());
        return resumeMapper.toProjectResponse(projectRepository.save(project));
    }

    public ProjectResponse updateProject(UpdateProjectRequest request, UUID projectId, UUID jsId) {
        Project project = getProject(projectId);
        if (!project.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        resumeMapper.updateProject(project, request);
        project.setUpdatedAt(LocalDateTime.now());
        return resumeMapper.toProjectResponse(projectRepository.save(project));
    }

    public void deleteProject(UUID projectId, UUID jsId) {
        Project project = getProject(projectId);
        if (!project.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        projectRepository.deleteById(projectId);
    }

    private Project getProject(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));
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
        certification.setCreatedAt(LocalDateTime.now());
        return resumeMapper.toCertificationResponse(certificationRepository.save(certification));
    }

    public CertificationResponse updateCertification(UpdateCertificationRequest request, UUID certId, UUID jsId) {
        Certification certification = getCertification(certId);
        if (!certification.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        resumeMapper.updateCertification(certification, request);
        certification.setUpdatedAt(LocalDateTime.now());
        return resumeMapper.toCertificationResponse(certificationRepository.save(certification));
    }

    public void deleteCertification(UUID certId, UUID jsId) {
        Certification certification = getCertification(certId);
        if (!certification.getJobSeeker().getId().equals(jsId)) {
            throw new UnauthorizedException("You do not have permission.");
        }
        certificationRepository.deleteById(certId);
    }

    private Certification getCertification(UUID certId) {
        return certificationRepository.findById(certId)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found."));
    }



    /*********** PRIVATE METHOD **********/

    private JobSeeker getJobSeeker(UUID jsId){
        return jobSeekerRepository.findById(jsId)
                .orElseThrow(() -> new BadRequestException("Job seeker not found with id: "+jsId));
    }

    private ResumeResponse resumeToResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .introduction(resume.getIntroduction())
                .objectCareer(resume.getObjectCareer())
                .educations(resume.getEducations().stream()
                        .map(resumeMapper::toEducationResponse)
                        .toList())
                .experiences(resume.getExperiences().stream()
                        .map(resumeMapper::toExperienceResponse)
                        .toList())
                .skills(resume.getSkills().stream()
                        .map(resumeMapper::toSkillResponse)
                        .toList())
                .certifications(resume.getCertifications().stream()
                        .map(resumeMapper::toCertificationResponse)
                        .toList())
                .awards(resume.getAwards().stream()
                        .map(resumeMapper::toAwardResponse)
                        .toList())
                .activities(resume.getActivities().stream()
                        .map(resumeMapper::toActivityResponse)
                        .toList())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .build();
    }

    private void addJobSeekerInfo(ResumeResponse response, JobSeekerResponse js){
        response.setFirstname(js.getFirstname());
        response.setLastname(js.getLastname());
        response.setEmail(js.getEmail());
        response.setPhone(js.getPhone());
        response.setAddress(js.getAddress());
        response.setDob(js.getDob());
        response.setGender(js.getGender());
    }

}
