package com.dev.job.dto.response.Posting;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobPostingPrompt(
        UUID id,
        String title,
        String companyName,
        String location,
        String description,
        String requirements,
        String experience,
        String type
) {}
