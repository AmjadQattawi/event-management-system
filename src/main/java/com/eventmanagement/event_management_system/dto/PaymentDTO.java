package com.eventmanagement.event_management_system.dto;

import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.enums.PaymentMethod;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentDTO extends BaseDTO{
    @Positive
    private Double amount;

//    @NotNull
    private LocalDateTime paymentDate;
    @NotNull
    private PaymentMethod paymentMethod;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PaymentStatus paymentStatus;

    private Long bookingId;

}
