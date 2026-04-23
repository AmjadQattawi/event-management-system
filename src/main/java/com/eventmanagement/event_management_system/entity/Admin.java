package com.eventmanagement.event_management_system.entity;

import com.eventmanagement.event_management_system.enums.Role;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.Where;

@DiscriminatorValue("Admin")
@Setter
@Getter
@Entity
public class Admin extends User{


}


