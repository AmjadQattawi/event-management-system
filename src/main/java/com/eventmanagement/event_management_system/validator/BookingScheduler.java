package com.eventmanagement.event_management_system.validator;


import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import com.eventmanagement.event_management_system.repository.BookingRepository;
import com.eventmanagement.event_management_system.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingScheduler {
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    @Value("${booking.expiration.minutes:15}")
    private int expirationMinutes;


    @Scheduled(fixedRateString = "${booking.cancel.rate}")// Every minute
    @Transactional
    public void cancelExpiredBookings() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(expirationMinutes);

        List<Booking> expiredBookings = bookingRepository
                .findAllByBookingStatusAndCreatedAtBefore(BookingStatus.PENDING, expirationTime);

        if (!expiredBookings.isEmpty()) {
            for (Booking booking : expiredBookings) {
                booking.setBookingStatus(BookingStatus.CANCELLED);

                if (booking.getPayment() != null) {
                    booking.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
                }

                 Event event = booking.getEvent();
                if (event != null) {
                    int updatedCapacity = event.getCapacity() + booking.getNumberOfTickets();
                    event.setCapacity(updatedCapacity);
                 }
            }
            bookingRepository.saveAll(expiredBookings);

        }
    }
}