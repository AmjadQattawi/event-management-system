package com.eventmanagement.event_management_system.searchCriteria;

import com.eventmanagement.event_management_system.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingSearchCriteria {

    private Long attendeeId;

    private Long eventId;

    private BookingStatus bookingStatus;

    private LocalDateTime bookingDateFrom;
    private LocalDateTime bookingDateTo;

    private Double minTotalPrice;
    private Double maxTotalPrice;



}
