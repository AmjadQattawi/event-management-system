package com.eventmanagement.event_management_system.dto;

import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingDTO extends BaseDTO {

    @Positive(message = "numberOfTickets must be positive")
    private Integer numberOfTickets;
    @Positive(message ="totalPrice must be positive" )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double totalPrice;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BookingStatus bookingStatus;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime bookingDate;
    @NotNull
    private Long attendeeId;

    @NotNull
    private Long eventId;

}
