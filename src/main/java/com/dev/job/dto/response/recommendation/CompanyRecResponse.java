package com.dev.job.dto.response.recommendation;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class CompanyRecResponse {
    private List<CompanyRecItem> recommendations;

    @Data
    public static class CompanyRecItem {
        @JsonProperty("company_id")
        private String companyId;

        @JsonProperty("company_name")
        private String companyName;

        private Double score;

        @JsonProperty("match_job_count")
        private int matchJobCount;
    }
}