package com.dev.job.mapper;

import com.dev.job.dto.request.Resume.*;
import com.dev.job.dto.response.Resume.*;
import com.dev.job.entity.resume.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
    Education toEducation(CreateEducationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEducation(@MappingTarget Education education, UpdateEducationRequest request);

    EducationResponse toEducationResponse(Education education);

    WorkExperience toExperience(CreateExperienceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateExperience(@MappingTarget WorkExperience experience, UpdateExperienceRequest request);

    ExperienceResponse toExperienceResponse(WorkExperience experience);

    Skill toSkill(CreateSkillRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSkill(@MappingTarget Skill skill, UpdateSkillRequest request);

    SkillResponse toSkillResponse(Skill skill);

    Activity toActivity(CreateActivityRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateActivity(@MappingTarget Activity activity, UpdateActivityRequest request);

    ActivityResponse toActivityResponse(Activity activity);

    Award toAward(CreateAwardRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAward(@MappingTarget Award award, UpdateAwardRequest request);

    AwardResponse toAwardResponse(Award award);

    Project toProject(CreateProjectRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProject(@MappingTarget Project project, UpdateProjectRequest request);

    ProjectResponse toProjectResponse(Project project);

    Certification toCertification(CreateCertificationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCertification(@MappingTarget Certification certification, UpdateCertificationRequest request);

    CertificationResponse toCertificationResponse(Certification certification);

}
