package com.eventmanagement.event_management_system.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReviewDTO extends BaseDTO{

    @Min(value = 1, message = "The rating must be at least 1")
    @Max(value = 5, message = "The rating cannot exceed 5")
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
    @NotNull
    private Long attendeeId;
    @NotNull
    private Long eventId;



}
