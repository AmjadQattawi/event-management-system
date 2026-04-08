package com.eventmanagement.event_management_system.repository;

import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>, JpaSpecificationExecutor<Booking> {

    Boolean existsByAttendeeIdAndEventIdAndBookingStatusNot(
            Long attendeeId,
            Long eventId,
            BookingStatus bookingStatus
    );
    List<Booking> findAllByBookingStatusAndCreatedAtBefore(BookingStatus status, LocalDateTime dateTime);



}
