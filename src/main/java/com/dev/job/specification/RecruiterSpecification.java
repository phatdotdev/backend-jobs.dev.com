package com.dev.job.specification;
import com.dev.job.entity.user.Recruiter;
import org.springframework.data.jpa.domain.Specification;

public class RecruiterSpecification {

    public static Specification<Recruiter> hasUsername(String username) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<Recruiter> hasEmail(String email) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Recruiter> hasPhone(String phone) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
    }

    public static Specification<Recruiter> hasCompanyName(String companyName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("companyName")), "%" + companyName.toLowerCase() + "%");
    }

}
