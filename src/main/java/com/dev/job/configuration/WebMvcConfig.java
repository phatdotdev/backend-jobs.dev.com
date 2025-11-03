package com.dev.job.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final String publicUrlPrefix = "/files/**";

    private String getFileUrl() {
        if (!uploadDir.endsWith("/")) {
            uploadDir += "/";
        }
        return "file:///" + uploadDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(publicUrlPrefix)
                .addResourceLocations(getFileUrl());
    }
}