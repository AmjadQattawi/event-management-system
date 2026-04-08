package com.eventmanagement.event_management_system.specification;

import com.eventmanagement.event_management_system.entity.Category;
import com.eventmanagement.event_management_system.searchCriteria.CategorySearchCriteria;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class CategorySpecification {

    public static Specification<Category> search(CategorySearchCriteria criteria) {
        List<Predicate> predicates = new ArrayList<>();
        return (root, query, cb) -> {
            if (StringUtils.hasText(criteria.getName())) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(criteria.getDescription())) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + criteria.getDescription() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
