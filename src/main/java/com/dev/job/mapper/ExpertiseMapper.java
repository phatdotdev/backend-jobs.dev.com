package com.dev.job.mapper;

import com.dev.job.dto.request.Expertise.CreateExpertiseRequest;
import com.dev.job.dto.request.Expertise.UpdateExpertiseRequest;
import com.dev.job.dto.response.Expertise.ExpertiseResponse;
import com.dev.job.entity.specification.Expertise;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface ExpertiseMapper {
    Expertise createRequestToExpertise(CreateExpertiseRequest request);
    Expertise updateRequestToExpertise(UpdateExpertiseRequest request);
    void updateExpertise(@MappingTarget  Expertise expertise, UpdateExpertiseRequest request);

    ExpertiseResponse toResponse(Expertise expertise);
}
