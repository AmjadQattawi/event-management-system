package com.eventmanagement.event_management_system.entity;

import com.eventmanagement.event_management_system.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("Organizer")
@Entity
public class Organizer extends User {


    private String companyName;

    @ManyToMany(mappedBy = "organizers")
    private List<Event> events = new ArrayList<>();


}
