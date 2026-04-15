package com.eventmanagement.event_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "base_seq", sequenceName = "Review_SEQ", allocationSize = 1)
public class Review extends BaseEntity{

    private Integer rating;
    @Column(name = "REVIEW_COMMENT")
    private String comment;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime reviewDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

}
