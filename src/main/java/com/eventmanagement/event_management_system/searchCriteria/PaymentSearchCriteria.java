package com.eventmanagement.event_management_system.searchCriteria;

import com.eventmanagement.event_management_system.enums.PaymentMethod;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentSearchCriteria {
    private Double minAmount;

    private Double maxAmount;

    private LocalDateTime paymentDateFrom;

    private LocalDateTime paymentDateTo;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String transactionId;

    private Long bookingId;

}
