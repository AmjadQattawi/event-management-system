package com.eventmanagement.event_management_system.repository;

import com.eventmanagement.event_management_system.entity.Review;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>, JpaSpecificationExecutor<Review> {

//    boolean hasConfirmedBooking(Long attendeeId, Long eventId, BookingStatus bookingStatus);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.attendee.id = :attendeeId AND b.event.id = :eventId AND b.bookingStatus = :status")
    boolean hasConfirmedBooking(
            @Param("attendeeId") Long attendeeId,
            @Param("eventId") Long eventId,
            @Param("status") BookingStatus status
    );

    Boolean existsByAttendeeIdAndEventId(Long attendeeId,Long eventId);

}
