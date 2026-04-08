package com.eventmanagement.event_management_system.repository;

import com.eventmanagement.event_management_system.entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer,Long>, JpaSpecificationExecutor<Organizer> {
}
