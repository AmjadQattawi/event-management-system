package com.eventmanagement.event_management_system.mapper;

import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends BaseMapper<Payment, PaymentDTO>{

    @Override
    @Mapping(source = "booking.id",target = "bookingId")
    public PaymentDTO toDTO(Payment payment);

    @Override
    @Mapping(target = "booking",ignore = true)
    public Payment toEntity(PaymentDTO paymentDTO);
}
