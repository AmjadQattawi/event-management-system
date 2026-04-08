package com.eventmanagement.event_management_system.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

@DiscriminatorValue("Admin")
@Setter
@Getter
@Entity
public class Admin extends User{


}


