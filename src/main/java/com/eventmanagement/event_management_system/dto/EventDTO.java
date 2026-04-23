package com.eventmanagement.event_management_system.dto;

import com.eventmanagement.event_management_system.enums.EventStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventDTO extends BaseDTO {
    @NotBlank
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @NotBlank
    private String city;
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @Positive(message = "Price must be positive")
    @NotNull
    private Double price;
    @Positive(message = "Capacity must be positive")
    @NotNull
    private Integer capacity;
    private EventStatus eventStatus;

    @NotNull
    private Long categoryId;
    @NotEmpty(message = "At least one organizer must be chosen for the event.")
    @NotNull
    private List<Long> organizerIds;

    private Double temperature;
        private Double windspeed;
}
