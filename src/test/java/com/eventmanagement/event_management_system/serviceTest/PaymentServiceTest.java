package com.eventmanagement.event_management_system.serviceTest;


import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.entity.Payment;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.enums.EventStatus;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import com.eventmanagement.event_management_system.mapper.PaymentMapper;
import com.eventmanagement.event_management_system.repository.BookingRepository;
import com.eventmanagement.event_management_system.repository.PaymentRepository;
import com.eventmanagement.event_management_system.service.PaymentService;
import com.eventmanagement.event_management_system.validator.PaymentNotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

@DisplayName("PaymentService Unit Tests")
public class PaymentServiceTest {


    @Mock private PaymentRepository paymentRepository;
    @Mock private PaymentMapper paymentMapper;
    @Mock private BookingRepository bookingRepository;
    @Mock private PaymentNotificationService paymentNotificationService;

    @InjectMocks
    private PaymentService paymentService;


    @Test
    @DisplayName("create: should set COMPLETED and confirm booking when amount matches")
    void create_ShouldSetCompleted_AndConfirmBooking_WhenAmountMatches() {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookingId(1L);
        dto.setAmount(200.0);

        Attendee attendee = new Attendee();
        attendee.setRewardPoints(10);

        Event event = new Event();
        event.setEventStatus(EventStatus.PUBLISHED);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setTotalPrice(200.0);  // matches payment amount
        booking.setNumberOfTickets(3);
        booking.setAttendee(attendee);
        booking.setEvent(event);

        Payment payment = new Payment();
        payment.setAmount(200.0);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        paymentService.create(dto);

        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    @DisplayName("create: should set FAILED when amount does NOT match totalPrice")
    void create_ShouldSetFailed_WhenAmountDoesNotMatch() {
        PaymentDTO dto = new PaymentDTO();
        dto.setBookingId(1L);
        dto.setAmount(150.0); // wrong amount

        Attendee attendee = new Attendee();
        attendee.setRewardPoints(0);

        Event event = new Event();
        event.setEventStatus(EventStatus.PUBLISHED);

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setTotalPrice(200.0); // totalPrice is 200, payment is 150 → mismatch
        booking.setNumberOfTickets(1);
        booking.setAttendee(attendee);
        booking.setEvent(event);

        Payment payment = new Payment();
        payment.setAmount(150.0);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(dto);

        paymentService.create(dto);

        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.PENDING); // not confirmed
    }


}
