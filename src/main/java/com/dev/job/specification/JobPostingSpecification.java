package com.dev.job.specification;

import com.dev.job.entity.posting.JobPosting;
import com.dev.job.entity.posting.JobType;
import com.dev.job.entity.posting.PostState;
import org.springframework.data.jpa.domain.Specification;

import javax.print.attribute.standard.JobState;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobPostingSpecification {
    public static Specification<JobPosting> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            return cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
        };
    }

    public static Specification<JobPosting> salaryBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) {
                return cb.between(root.get("promotedSalary"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("promotedSalary"), min);
            } else {
                return cb.lessThanOrEqualTo(root.get("promotedSalary"), max);
            }
        };
    }

    public static Specification<JobPosting> locationIs(UUID locationId) {
        return (root, query, cb) -> {
            if (locationId == null) return null;
            return cb.equal(root.get("location").get("id"), locationId);
        };
    }

    public static Specification<JobPosting> typeIs(JobType type) {
        return (root, query, cb) -> {
            if (type == null) return null;
            return cb.equal(root.get("type"), type);
        };
    }

    public static  Specification<JobPosting> stateIs(PostState state){
        return (root, query, cb) -> {
            if(state == null) return null;
            return cb.equal(root.get("state"), state);
        };
    }

    public static Specification<JobPosting> buildSpec(
            String keyword,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            UUID locationId,
            JobType type,
            PostState state
    ) {
        List<Specification<JobPosting>> specs = new ArrayList<>();

        specs.add(hasKeyword(keyword));
        specs.add(salaryBetween(minSalary, maxSalary));
        specs.add(locationIs(locationId));
        specs.add(typeIs(type));
        specs.add(stateIs(state));

        return Specification.allOf(specs);
    }
}
