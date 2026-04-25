package com.eventmanagement.event_management_system.serviceIntegerationTest;


import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.entity.*;
import com.eventmanagement.event_management_system.enums.*;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.repository.*;
import com.eventmanagement.event_management_system.service.BookingService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
@DisplayName("BookingService — Integration Tests")
class BookingServiceIntegrationTest {

    @Autowired private BookingService    bookingService;
    @Autowired private BookingRepository  bookingRepository;
    @Autowired private EventRepository    eventRepository;
    @Autowired private AttendeeRepository attendeeRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private PaymentRepository  paymentRepository;

    private Event    savedEvent;
    private Attendee savedAttendee;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setName("Tech");
        categoryRepository.save(category);

        savedEvent = new Event();
        savedEvent.setName("Spring Boot Workshop");
        savedEvent.setCapacity(10);
        savedEvent.setPrice(50.0);
        savedEvent.setEventStatus(EventStatus.PUBLISHED);
        savedEvent.setStartDate(LocalDateTime.now().plusDays(1));
        savedEvent.setEndDate(LocalDateTime.now().plusDays(2));
        savedEvent.setCategory(category);
        savedEvent = eventRepository.save(savedEvent);

        savedAttendee = new Attendee();
        savedAttendee.setEmail("ahmed@test.com");
        savedAttendee.setPassword("encoded_pass");
        savedAttendee.setRole(Role.ATTENDEE);
        savedAttendee.setRewardPoints(10);
        savedAttendee = attendeeRepository.save(savedAttendee);
    }

    // ══════════════════════════════════════════════════════
    //  CREATE
    // ══════════════════════════════════════════════════════

    @Test
    void create_ShouldDecrementEventCapacityInDatabase() {
        BookingDTO dto = buildBookingDTO(2);

        BookingDTO result = bookingService.create(dto);

        Event updatedEvent = eventRepository.findById(savedEvent.getId()).orElseThrow();
        assertThat(updatedEvent.getCapacity()).isEqualTo(8);
        assertThat(result.getTotalPrice()).isEqualTo(100.0);
        assertThat(result.getBookingStatus()).isEqualTo(BookingStatus.PENDING);
    }

    @Test
    void create_ShouldPersistBookingWithCorrectRelations() {
        BookingDTO dto = buildBookingDTO(1);

        BookingDTO result = bookingService.create(dto);

        Booking saved = bookingRepository.findById(result.getId()).orElseThrow();
        assertThat(saved.getEvent().getId()).isEqualTo(savedEvent.getId());
        assertThat(saved.getAttendee().getId()).isEqualTo(savedAttendee.getId());
        assertThat(saved.getBookingStatus()).isEqualTo(BookingStatus.PENDING);
    }

    @Test
    void create_ShouldCalculateTotalPriceCorrectly() {
        BookingDTO dto = buildBookingDTO(3);

        BookingDTO result = bookingService.create(dto);

        assertThat(result.getTotalPrice()).isEqualTo(150.0);
    }

    @Test
    void create_ShouldThrow_WhenEventNotFound() {
        BookingDTO dto = buildBookingDTO(1);
        dto.setEventId(9999L);

        assertThatThrownBy(() -> bookingService.create(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Event not found");
    }

    @Test
    void create_ShouldThrow_WhenAttendeeNotFound() {
        BookingDTO dto = buildBookingDTO(1);
        dto.setAttendeeId(9999L);

        assertThatThrownBy(() -> bookingService.create(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Attendee not found");
    }

    // ══════════════════════════════════════════════════════
    //  DELETE
    // ══════════════════════════════════════════════════════

    @Test
    void delete_ShouldRestoreCapacity_WhenBookingWasPending() {
        Booking booking = persistBooking(2, BookingStatus.PENDING);
        assertThat(eventRepository.findById(savedEvent.getId())
                .orElseThrow().getCapacity()).isEqualTo(8);

        bookingService.delete(booking.getId());

        assertThat(eventRepository.findById(savedEvent.getId())
                .orElseThrow().getCapacity()).isEqualTo(10);     // رجع
        assertThat(bookingRepository.findById(booking.getId())).isEmpty(); // اتمسح
    }

    @Test
    void delete_ShouldRefundPayment_WhenBookingWasConfirmed() {
        Booking booking = persistBooking(1, BookingStatus.CONFIRMED);

        Payment payment = new Payment();
        payment.setAmount(50.0);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setBooking(booking);
        paymentRepository.save(payment);
        booking.setPayment(payment);
        bookingRepository.save(booking);

        bookingService.delete(booking.getId());

        assertThat(paymentRepository.findById(payment.getId())
                .orElseThrow().getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
    }

    @Test
    void delete_ShouldNotRestoreCapacity_WhenBookingWasCancelled() {
        savedEvent.setCapacity(8);
        eventRepository.save(savedEvent);
        Booking booking = persistBooking(2, BookingStatus.CANCELLED);

        bookingService.delete(booking.getId());

        assertThat(eventRepository.findById(savedEvent.getId())
                .orElseThrow().getCapacity()).isEqualTo(8); // ما تغير
    }

    @Test
    void delete_ShouldThrow_WhenBookingNotFound() {
        assertThatThrownBy(() -> bookingService.delete(9999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ══════════════════════════════════════════════════════
    //  Helpers
    // ══════════════════════════════════════════════════════

    private BookingDTO buildBookingDTO(int tickets) {
        BookingDTO dto = new BookingDTO();
        dto.setEventId(savedEvent.getId());
        dto.setAttendeeId(savedAttendee.getId());
        dto.setNumberOfTickets(tickets);
        return dto;
    }

    private Booking persistBooking(int tickets, BookingStatus status) {
        savedEvent.setCapacity(savedEvent.getCapacity() - tickets);
        eventRepository.save(savedEvent);

        Booking booking = new Booking();
        booking.setEvent(savedEvent);
        booking.setAttendee(savedAttendee);
        booking.setNumberOfTickets(tickets);
        booking.setTotalPrice(tickets * savedEvent.getPrice());
        booking.setBookingStatus(status);
        return bookingRepository.save(booking);
    }
}