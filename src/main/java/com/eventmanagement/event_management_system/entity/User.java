package com.eventmanagement.event_management_system.entity;

import com.eventmanagement.event_management_system.enums.Role;
import com.eventmanagement.event_management_system.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Entity
@Table(name = "USERS_TBL")
@SoftDelete
@SequenceGenerator(name = "base_seq", sequenceName = "User_SEQ", allocationSize = 1)
public abstract class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus=UserStatus.ACTIVE;

//    @Enumerated(EnumType.STRING)
//    private Role role;
}
