package com.eventmanagement.event_management_system.validator;

import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.exception.InvalidEventDataException;
import com.eventmanagement.event_management_system.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventValidator {

    private final EventRepository eventRepository;

    public void validateEventDates(EventDTO eventDTO) {
            if (eventDTO.getEndDate().isBefore(eventDTO.getStartDate())) {
                throw new InvalidEventDataException("End date must be after start date");
            }

    }

}
