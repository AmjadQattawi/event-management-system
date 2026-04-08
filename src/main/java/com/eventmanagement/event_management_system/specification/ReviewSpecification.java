package com.eventmanagement.event_management_system.specification;

import com.eventmanagement.event_management_system.entity.Review;
import com.eventmanagement.event_management_system.searchCriteria.ReviewSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification {

    public static Specification<Review> search(ReviewSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

             if (criteria.getMinRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), criteria.getMinRating()));
            }
            if (criteria.getMaxRating() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("rating"), criteria.getMaxRating()));
            }

             if (StringUtils.hasText(criteria.getComment())) {
                predicates.add(cb.like(cb.lower(root.get("comment")), "%" + criteria.getComment().toLowerCase() + "%"));
            }

             if (criteria.getReviewDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("reviewDate"), criteria.getReviewDateFrom()));
            }
            if (criteria.getReviewDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("reviewDate"), criteria.getReviewDateTo()));
            }

             if (criteria.getAttendeeId() != null) {
                predicates.add(cb.equal(root.get("attendee").get("id"), criteria.getAttendeeId()));
            }
             if (criteria.getEventId() != null) {
                predicates.add(cb.equal(root.get("event").get("id"), criteria.getEventId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
