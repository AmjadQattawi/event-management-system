package com.eventmanagement.event_management_system.specification;

import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.searchCriteria.BookingSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class BookingSpecification {

    public static Specification<Booking> search(BookingSearchCriteria criteria){
        return (root, query, cb) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (criteria.getAttendeeId()!=null){
                predicates.add(cb.equal(root.get("attendeeId"),criteria.getAttendeeId()));
            }
          if (criteria.getEventId()!=null){
              predicates.add(cb.equal(root.get("eventId"),criteria.getEventId()));
          }
          if (criteria.getBookingStatus()!=null){
              predicates.add(cb.equal(root.get("bookingStatus"),criteria.getBookingStatus()));
          }
            if (criteria.getBookingDateFrom()!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("bookingDateFrom"),criteria.getBookingDateFrom()));
            }
            if (criteria.getBookingDateTo()!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("bookingDateTo"),criteria.getBookingDateTo()));
            }
            if(criteria.getMinTotalPrice()!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalPrice"),criteria.getMinTotalPrice()));
            }
            if (criteria.getMaxTotalPrice()!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("totalPrice"),criteria.getMaxTotalPrice()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
