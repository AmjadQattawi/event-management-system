package com.eventmanagement.event_management_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "base_seq", sequenceName = "CATEGORY_SEQ", allocationSize = 1)
@SoftDelete
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Event> events = new ArrayList<>();
}
