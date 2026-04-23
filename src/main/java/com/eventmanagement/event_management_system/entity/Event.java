package com.eventmanagement.event_management_system.entity;

import com.eventmanagement.event_management_system.enums.EventStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "base_seq", sequenceName = "EVENT_SEQ", allocationSize = 1)
@SoftDelete
public class Event extends BaseEntity{

    @Column(nullable = false,unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Future(message = "End date must be in the future")
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus=EventStatus.DRAFT;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "organizer_events",
            joinColumns =@JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "organizer_id")
    )
    private List<Organizer> organizers=new ArrayList<>();

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<Booking> booking=new ArrayList<>();

    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews=new ArrayList<>();

    private Double temperature;
    private Double windspeed;


}
