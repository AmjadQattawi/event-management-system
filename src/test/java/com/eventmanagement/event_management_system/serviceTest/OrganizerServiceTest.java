package com.eventmanagement.event_management_system.serviceTest;



import com.eventmanagement.event_management_system.dto.OrganizerDTO;
import com.eventmanagement.event_management_system.entity.Organizer;
import com.eventmanagement.event_management_system.enums.Role;
import com.eventmanagement.event_management_system.enums.UserStatus;
import com.eventmanagement.event_management_system.mapper.OrganizerMapper;
import com.eventmanagement.event_management_system.repository.OrganizerRepository;
import com.eventmanagement.event_management_system.service.OrganizerService;
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
@DisplayName("OrganizerService Unit Tests")
class OrganizerServiceTest {

    @Mock
    private OrganizerRepository organizerRepository;
    @Mock
    private OrganizerMapper organizerMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OrganizerService organizerService;

    // ===================== CREATE =====================

    @Test
    @DisplayName("create: should set Role=ORGANIZER and encode password")
    void create_ShouldSetRoleOrganizerAndEncodePassword() {
        OrganizerDTO dto = new OrganizerDTO();
        dto.setEmail("org@test.com");
        dto.setPassword("pass");

        Organizer organizer = new Organizer();

        when(organizerRepository.findByEmail("org@test.com")).thenReturn(Optional.empty());
        when(organizerMapper.toEntity(dto)).thenReturn(organizer);
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(organizerRepository.save(organizer)).thenReturn(organizer);
        when(organizerMapper.toDTO(organizer)).thenReturn(dto);

        organizerService.create(dto);

        assertThat(organizer.getRole()).isEqualTo(Role.ORGANIZER);
        assertThat(organizer.getPassword()).isEqualTo("encodedPass");
    }

    @Test
    @DisplayName("create: should set UserStatus=ACTIVE when status is null")
    void create_ShouldSetStatusActive_WhenStatusIsNull() {
        OrganizerDTO dto = new OrganizerDTO();
        dto.setEmail("org@test.com");
        dto.setPassword("pass");
        Organizer organizer = new Organizer(); // userStatus = null

        when(organizerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(organizerMapper.toEntity(dto)).thenReturn(organizer);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(organizerRepository.save(organizer)).thenReturn(organizer);
        when(organizerMapper.toDTO(organizer)).thenReturn(dto);

        organizerService.create(dto);

        assertThat(organizer.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}