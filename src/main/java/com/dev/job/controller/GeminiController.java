package com.dev.job.controller;

import com.dev.job.service.GeminiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GeminiController {

    GeminiService geminiService;

    @GetMapping("/suggest-jobs")
    public String suggest(@RequestParam String resume) {
        return geminiService.suggestTop5Jobs(resume);
    }
}
