package com.eventmanagement.event_management_system.security;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.dto.UserDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.enums.Role;
import com.eventmanagement.event_management_system.exception.DuplicateResourceException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService; // inject


    public void register(AttendeeDTO attendeeDTO) {
        if (userRepository.findByEmail(attendeeDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Sorry, this email is already registered in the system!");
        }

        Attendee attendee = new Attendee();
        attendee.setEmail(attendeeDTO.getEmail());
        attendee.setRole(Role.ATTENDEE);
        attendee.setPassword(passwordEncoder.encode(attendeeDTO.getPassword()));
        attendee.setRewardPoints(10);
        userRepository.save(attendee);
    }


    public String login(UserDTO userDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getEmail(),
                            userDTO.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(userDetails);

        } catch (BadCredentialsException e) {
            throw new ResourceNotFoundException("Incorrect email or password");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Login failed");
        }
    }

    }


