package com.eventmanagement.event_management_system.repository;

import com.eventmanagement.event_management_system.entity.Admin;
import com.eventmanagement.event_management_system.entity.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee,Long>, JpaSpecificationExecutor<Attendee> {

    Optional<Attendee> findByEmail(String email);


}
