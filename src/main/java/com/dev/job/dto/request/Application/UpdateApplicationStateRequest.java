package com.dev.job.dto.request.Application;

import com.dev.job.entity.application.ApplicationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateApplicationStateRequest {
    ApplicationState state;
    String content;
}
