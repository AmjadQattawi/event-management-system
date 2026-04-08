package com.eventmanagement.event_management_system.entity;

import com.eventmanagement.event_management_system.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.boot.jaxb.mapping.LifecycleCallback;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking extends BaseEntity{

    @Column(nullable = false)
    private Integer numberOfTickets;
    @Version
    private Long version;
    private Double totalPrice;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus=BookingStatus.PENDING;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime bookingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Attendee attendee;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY)
    private Payment payment;


}
