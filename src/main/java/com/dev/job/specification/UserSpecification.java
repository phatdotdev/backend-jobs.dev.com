package com.dev.job.specification;

import com.dev.job.entity.user.User;
import com.dev.job.entity.user.UserRole;
import com.dev.job.entity.user.UserStatus;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> hasStatus(String status) {
        return (root, query, cb) -> {
            try {
                if (status == null || status.isBlank()) {
                    return cb.conjunction();
                }
                UserStatus parsedStatus = UserStatus.valueOf(status.toUpperCase());
                return cb.equal(root.get("status"), parsedStatus);
            } catch (IllegalArgumentException e) {
                return cb.conjunction();
            }
        };
    }

    public static Specification<User> hasRole(String role) {
        return (root, query, cb) -> {
            try {
                if (role == null || role.isBlank()) {
                    return cb.conjunction();
                }
                UserRole parsedRole = UserRole.valueOf(role.toUpperCase());
                return cb.equal(root.get("role"), parsedRole);
            } catch (IllegalArgumentException e) {
                return cb.conjunction();
            }
        };
    }
}
