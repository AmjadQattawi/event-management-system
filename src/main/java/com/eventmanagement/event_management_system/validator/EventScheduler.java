package com.eventmanagement.event_management_system.validator;


import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.enums.EventStatus;
import com.eventmanagement.event_management_system.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventScheduler {

    private final EventRepository eventRepository;

    @Scheduled(cron = "${event.update.cron}") // Every hour
    @Transactional
    public void updateCompletedEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findByEndDateBeforeAndEventStatus(now, EventStatus.PUBLISHED);
        for (Event event : events) {
            event.setEventStatus(EventStatus.COMPLETED);
        }
        eventRepository.saveAll(events);
    }
}