package com.eventmanagement.event_management_system.specification;

import com.eventmanagement.event_management_system.entity.Organizer;
import com.eventmanagement.event_management_system.searchCriteria.OrganizerSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class OrganizerSpecification {

    public static Specification<Organizer> search(OrganizerSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getCompanyName() != null) {
                predicates.add(cb.like(cb.lower(root.get("companyName")), criteria.getCompanyName()));
            }
            if (criteria.getTaxId() != null) {
                predicates.add(cb.like(cb.lower(root.get("taxId")), criteria.getTaxId()));
            }
            if (criteria.getVerified() != null) {
                predicates.add(cb.equal(cb.lower(root.get("Verified")), criteria.getVerified()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));

        };

    }

}
