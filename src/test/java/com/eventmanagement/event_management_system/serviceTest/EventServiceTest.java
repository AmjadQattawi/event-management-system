package com.eventmanagement.event_management_system.serviceTest;


import com.eventmanagement.event_management_system.entity.*;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import com.eventmanagement.event_management_system.mapper.EventMapper;
import com.eventmanagement.event_management_system.repository.CategoryRepository;
import com.eventmanagement.event_management_system.repository.EventRepository;
import com.eventmanagement.event_management_system.repository.OrganizerRepository;
import com.eventmanagement.event_management_system.service.EventService;
import com.eventmanagement.event_management_system.validator.EventValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventService Unit Tests")
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock private EventMapper eventMapper;
    @Mock private CategoryRepository categoryRepository;
    @Mock private OrganizerRepository organizerRepository;
    @Mock private EventValidator eventValidator;
    @Mock private RestTemplate restTemplate;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("delete: should cancel pending/confirmed bookings and refund payments on delete")
    void delete_ShouldCancelBookingsAndRefundPayments() {

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.COMPLETED);

        Booking confirmedBooking = new Booking();
        confirmedBooking.setBookingStatus(BookingStatus.CONFIRMED);
        confirmedBooking.setPayment(payment);

        Event event = new Event();
        event.setBooking(new ArrayList<>(List.of(confirmedBooking)));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.saveAndFlush(event)).thenReturn(event);

        eventService.delete(1L);

        assertThat(confirmedBooking.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
        verify(eventRepository).delete(event);
    }



}
