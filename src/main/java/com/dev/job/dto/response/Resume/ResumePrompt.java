package com.dev.job.dto.response.Resume;

import java.util.List;

public record ResumePrompt(String address,
                           String introduction,
                           String objectCareer,
                           List<String> educations,
                           List<String> experiences,
                           List<String> skills)
{}
