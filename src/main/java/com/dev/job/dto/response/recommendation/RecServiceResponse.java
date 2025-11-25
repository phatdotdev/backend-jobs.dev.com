package com.dev.job.dto.response.recommendation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class RecServiceResponse {
    List<RecommendationItem> recommendations;

    @Data
    public static class RecommendationItem {
        @JsonProperty("job_id")
        private String jobId;

        private Double score;
    }
}