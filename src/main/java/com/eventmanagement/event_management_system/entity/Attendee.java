package com.eventmanagement.event_management_system.entity;

import com.eventmanagement.event_management_system.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.List;


@DiscriminatorValue("Attendee")
@Setter
@Getter
@Entity
public class Attendee extends User{
    private Integer rewardPoints=0;

    @OneToMany(mappedBy = "attendee")
    private List<Booking>bookings=new ArrayList<>();

    @OneToMany(mappedBy = "attendee",cascade =  CascadeType.REMOVE)
    private List<Review>reviews=new ArrayList<>();


}
