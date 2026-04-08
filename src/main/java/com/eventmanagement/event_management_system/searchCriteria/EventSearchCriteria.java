package com.eventmanagement.event_management_system.searchCriteria;

import com.eventmanagement.event_management_system.enums.EventStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventSearchCriteria {

    private String name;
    private String description;
    private LocalDateTime startDateFrom;
    private LocalDateTime startDateTo;
    private String location;
    private Double minPrice;
    private Double maxPrice;
    private Integer minCapacity;
    private EventStatus eventStatus;
    private Long categoryId;
}