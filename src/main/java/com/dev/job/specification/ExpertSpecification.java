package com.dev.job.specification;

import com.dev.job.entity.user.Expert;
import com.dev.job.entity.user.User;
import org.springframework.data.jpa.domain.Specification;

public class ExpertSpecification {
    public static Specification<Expert> hasUsername(String username) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<Expert> hasEmail(String email) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Expert> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
