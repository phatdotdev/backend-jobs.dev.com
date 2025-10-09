package com.dev.job.mapper;

import com.dev.job.dto.request.Resume.*;
import com.dev.job.dto.response.Resume.*;
import com.dev.job.entity.resume.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
    Education toEducation(CreateEducationRequest request);
    EducationResponse toEducationResponse(Education education);

    WorkExperience toExperience(CreateExperienceRequest request);
    ExperienceResponse toExperienceResponse(WorkExperience experience);

    Skill toSkill(CreateSkillRequest request);
    SkillResponse toSkillResponse(Skill skill);

    Activity toActivity(CreateActivityRequest request);
    ActivityResponse toActivityResponse(Activity activity);

    Award toAward(CreateAwardRequest request);
    AwardResponse toAwardResponse(Award award);

    Project toProject(CreateProjectRequest request);
    ProjectResponse toProjectResponse(Project project);

    Resume toResume(CreateResumeRequest request);
    ResumeResponse toResumeResponse(Resume resume);
}
