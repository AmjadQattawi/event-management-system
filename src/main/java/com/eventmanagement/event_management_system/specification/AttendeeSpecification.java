package com.eventmanagement.event_management_system.specification;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.searchCriteria.AttendeeSearshCriteria;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class AttendeeSpecification {

    public static Specification<Attendee> search(AttendeeSearshCriteria criteria){
        return (root, query, cb) -> {
            List<Predicate> predicates= new ArrayList<>();
            if (StringUtils.hasText(criteria.getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(criteria.getEmail())) {
                predicates.add(cb.like(cb.lower(root.get("email")),
                        "%" + criteria.getEmail().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(criteria.getPhone())) {
                predicates.add(cb.like(root.get("phone"), "%" + criteria.getPhone() + "%"));
            }
            if (criteria.getUserStatus() != null) {
                predicates.add(cb.equal(root.get("userStatus"), criteria.getUserStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }



}
