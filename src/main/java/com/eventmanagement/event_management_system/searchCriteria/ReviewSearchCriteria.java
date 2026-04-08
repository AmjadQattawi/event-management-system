package com.eventmanagement.event_management_system.searchCriteria;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewSearchCriteria {

    private Integer minRating;
    private Integer maxRating;
    private String comment;
    private LocalDateTime reviewDateFrom;
    private LocalDateTime reviewDateTo;
    private Long attendeeId;
    private Long eventId;

}
