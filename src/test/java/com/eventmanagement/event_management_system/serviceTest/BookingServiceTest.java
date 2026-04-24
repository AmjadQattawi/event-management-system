package com.eventmanagement.event_management_system.serviceTest;


import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.mapper.BookingMapper;
import com.eventmanagement.event_management_system.repository.AttendeeRepository;
import com.eventmanagement.event_management_system.repository.BookingRepository;
import com.eventmanagement.event_management_system.repository.EventRepository;
import com.eventmanagement.event_management_system.service.BookingService;
import com.eventmanagement.event_management_system.validator.BookingValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingService Unit Tests")

public class BookingServiceTest {


    @Mock private BookingRepository bookingRepository;
    @Mock private BookingMapper bookingMapper;
    @Mock private EventRepository eventRepository;
    @Mock private BookingValidator bookingValidator;
    @Mock private AttendeeRepository attendeeRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("create: should calculate totalPrice and decrement event capacity")
    void create_ShouldCalculateTotalPriceAndDecrementCapacity() {
        // Given
        BookingDTO dto = new BookingDTO();
        dto.setEventId(1L);
        dto.setAttendeeId(1L);
        dto.setNumberOfTickets(2);

        Event event = new Event();
        event.setCapacity(10);
        event.setPrice(100.0);

        Attendee attendee = new Attendee();
        Booking booking = new Booking();
        booking.setNumberOfTickets(2);

        when(eventRepository.findByIdWithLock(1L)).thenReturn(Optional.of(event));
        when(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee));
        when(bookingMapper.toEntity(dto)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDTO(booking)).thenReturn(dto);

        bookingService.create(dto);

        assertThat(booking.getTotalPrice()).isEqualTo(200.0);   // 2 tickets * 100 price
        assertThat(event.getCapacity()).isEqualTo(8);           // 10 - 2
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.PENDING);
    }

    @Test
    @DisplayName("create: should call all validators before saving")
    void create_ShouldCallAllValidators() {
        BookingDTO dto = new BookingDTO();
        dto.setEventId(1L);
        dto.setAttendeeId(1L);
        dto.setNumberOfTickets(1);

        Event event = new Event();
        event.setCapacity(5);
        event.setPrice(50.0);
        Booking booking = new Booking();
        booking.setNumberOfTickets(1);

        when(eventRepository.findByIdWithLock(1L)).thenReturn(Optional.of(event));
        when(attendeeRepository.findById(1L)).thenReturn(Optional.of(new Attendee()));
        when(bookingMapper.toEntity(dto)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDTO(booking)).thenReturn(dto);

        bookingService.create(dto);

        verify(bookingValidator).checkStatus(event);
        verify(bookingValidator).checkExistsByAttendeeIdAndEventId(dto);
        verify(bookingValidator).validateCapacity(dto, event);
    }

    @Test
    @DisplayName("create: should throw ResourceNotFoundException when event not found")
    void create_ShouldThrowResourceNotFoundException_WhenEventNotFound() {
         BookingDTO dto = new BookingDTO();
        dto.setEventId(99L);

        when(eventRepository.findByIdWithLock(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.create(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Event not found");

        verify(bookingRepository, never()).save(any());
    }


}
