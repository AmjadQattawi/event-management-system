package com.eventmanagement.event_management_system.serviceTest;



import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.enums.Role;
import com.eventmanagement.event_management_system.enums.UserStatus;
import com.eventmanagement.event_management_system.mapper.AttendeeMapper;
import com.eventmanagement.event_management_system.repository.AttendeeRepository;
import com.eventmanagement.event_management_system.service.AttendeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AttendeeService Unit Tests")
public class AttendeeServiceTest {


    @Mock private AttendeeRepository attendeeRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AttendeeMapper attendeeMapper;

    @InjectMocks
    private AttendeeService attendeeService;

    @Test
    @DisplayName("create: should give new attendee 10 reward points on registration")
    void create_ShouldSetRewardPointsTo10_WhenNewAttendeeRegisters() {
        AttendeeDTO dto = new AttendeeDTO();
        dto.setEmail("sara@test.com");
        dto.setPassword("pass");

        Attendee attendee = new Attendee();
        AttendeeDTO savedDto = new AttendeeDTO();

        when(attendeeRepository.findByEmail("sara@test.com")).thenReturn(Optional.empty());
        when(attendeeMapper.toEntity(dto)).thenReturn(attendee);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(attendeeRepository.save(attendee)).thenReturn(attendee);
        when(attendeeMapper.toDTO(attendee)).thenReturn(savedDto);

        attendeeService.create(dto);

        assertThat(attendee.getRewardPoints()).isEqualTo(10);
    }

    @Test
    @DisplayName("create: should set Role=ATTENDEE and Status=ACTIVE")
    void create_ShouldSetRoleAttendeeAndStatusActive() {
        AttendeeDTO dto = new AttendeeDTO();
        dto.setEmail("test@test.com");
        dto.setPassword("pass");
        Attendee attendee = new Attendee();

        when(attendeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(attendeeMapper.toEntity(dto)).thenReturn(attendee);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(attendeeRepository.save(attendee)).thenReturn(attendee);
        when(attendeeMapper.toDTO(attendee)).thenReturn(dto);

        attendeeService.create(dto);

        assertThat(attendee.getRole()).isEqualTo(Role.ATTENDEE);
        assertThat(attendee.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}
