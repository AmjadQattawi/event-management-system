package com.eventmanagement.event_management_system.entity;

import com.eventmanagement.event_management_system.enums.PaymentMethod;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "base_seq", sequenceName = "Payment_SEQ", allocationSize = 1)
@SoftDelete
public class Payment extends BaseEntity{
    @Column(nullable = false)
    private Double amount;

    @CreationTimestamp
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus=PaymentStatus.PENDING;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private Booking booking;

}
