package com.eventmanagement.event_management_system.specification;

import com.eventmanagement.event_management_system.entity.Payment;
import com.eventmanagement.event_management_system.searchCriteria.PaymentSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PaymentSpecification {

    public static Specification<Payment> search(PaymentSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), criteria.getMinAmount()));
            }
            if (criteria.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), criteria.getMaxAmount()));
            }

            if (criteria.getPaymentDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("paymentDate"), criteria.getPaymentDateFrom()));
            }
            if (criteria.getPaymentDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("paymentDate"), criteria.getPaymentDateTo()));
            }

            if (criteria.getPaymentMethod() != null) {
                predicates.add(cb.equal(root.get("paymentMethod"), criteria.getPaymentMethod()));
            }

            if (criteria.getPaymentStatus() != null) {
                predicates.add(cb.equal(root.get("paymentStatus"), criteria.getPaymentStatus()));
            }

            if (StringUtils.hasText(criteria.getTransactionId())) {
                predicates.add(cb.like(root.get("transactionId"), "%" + criteria.getTransactionId() + "%"));
            }

            // 6. رقم الحجز (Booking ID - علاقة Many-to-One)
            if (criteria.getBookingId() != null) {
                predicates.add(cb.equal(root.get("booking").get("id"), criteria.getBookingId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}
