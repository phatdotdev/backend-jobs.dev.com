package com.dev.job.dto.response.User;

import java.util.List;
import java.util.UUID;

public record CandidatePrompt(
        UUID id,
        String fullName,
        String phone,
        String email,
        String address,
        List<String> educations,
        List<String> experiences,
        List<String> skill
) {
}
