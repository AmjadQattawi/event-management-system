package com.eventmanagement.event_management_system.repository;

import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long>, JpaSpecificationExecutor<Event> {

    List<Event> findByEndDateBeforeAndEventStatus(LocalDateTime now, EventStatus status);

    boolean existsByName(String name);

}
