package com.eventmanagement.event_management_system.repository;

import com.eventmanagement.event_management_system.dto.UserDTO;
import com.eventmanagement.event_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

}
