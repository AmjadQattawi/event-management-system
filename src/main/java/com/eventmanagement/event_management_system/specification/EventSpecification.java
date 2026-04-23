package com.eventmanagement.event_management_system.specification;

import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.searchCriteria.EventSearchCriteria;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.util.StringUtils;

public class EventSpecification {


    public static Specification<Event> search(EventSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(criteria.getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(criteria.getDescription())){
                predicates.add(cb.like(cb.lower(root.get("description")),"%"+criteria.getDescription()+"%"));
            }
            if (StringUtils.hasText(criteria.getCity())) {
                predicates.add(cb.like(cb.lower(root.get("location")),
                        "%" + criteria.getCity().toLowerCase() + "%"));
            }
            if (criteria.getStartDateFrom()!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"),criteria.getStartDateFrom()));
            }
            if (criteria.getStartDateTo()!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"),criteria.getStartDateTo()));
            }
            if (criteria.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
            }
            if (criteria.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
            }
            if (criteria.getMinCapacity() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("capacity"), criteria.getMinCapacity()));
            }
            if (criteria.getEventStatus()!=null){
                predicates.add(cb.equal(root.get("capacity"),criteria.getMinCapacity()));
            }
            if (criteria.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), criteria.getCategoryId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
