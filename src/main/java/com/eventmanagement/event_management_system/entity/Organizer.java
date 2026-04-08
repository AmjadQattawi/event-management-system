package com.eventmanagement.event_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("Organizer")
@Entity
public class Organizer extends User{

    //@Column(nullable = false)
    private String companyName;


    @ManyToMany(mappedBy = "organizers")
    private List<Event> events=new ArrayList<>();


}
