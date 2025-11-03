package com.dev.job.specification;

import com.dev.job.entity.user.JobSeeker;
import org.springframework.data.jpa.domain.Specification;

public class JobSeekerSpecification {
    public static Specification<JobSeeker> hasUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isEmpty()) return null;
            return cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
        };
    }

    public static Specification<JobSeeker> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isEmpty()) return null;
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    public static Specification<JobSeeker> hasPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.isEmpty()) return null;
            return cb.equal(cb.lower(root.get("phone")), phone.toLowerCase());
        };
    }
}
