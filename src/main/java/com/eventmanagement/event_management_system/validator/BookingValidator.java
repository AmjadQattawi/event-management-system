package com.eventmanagement.event_management_system.validator;

import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.enums.EventStatus;
import com.eventmanagement.event_management_system.exception.DuplicateResourceException;
import com.eventmanagement.event_management_system.exception.IllegalStatusException;
import com.eventmanagement.event_management_system.exception.InsufficientCapacityException;
import com.eventmanagement.event_management_system.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingValidator {

    private final BookingRepository bookingRepository;

    public void checkStatus(Event event){
        if (event.getEventStatus()!= EventStatus.PUBLISHED)
            throw new IllegalStatusException("Event Statue must be PUBLISHED");
    }

    public void validateCapacity(BookingDTO bookingDTO,Event event){
        if (bookingDTO.getNumberOfTickets()>event.getCapacity())
            throw new InsufficientCapacityException("There are no seats remaining for this number (no capacity) ");
    }

    public Double getTotalPrice(Integer numberOfTickets,Double price){
        return numberOfTickets*price;
    }


    public void checkExistsByAttendeeIdAndEventId(BookingDTO bookingDTO) {
        if (bookingRepository.existsByAttendeeIdAndEventIdAndBookingStatusNot(
                bookingDTO.getAttendeeId(),
                bookingDTO.getEventId(),
                BookingStatus.CANCELLED
        ))
            throw new DuplicateResourceException("You already booked this event");
        }



}
