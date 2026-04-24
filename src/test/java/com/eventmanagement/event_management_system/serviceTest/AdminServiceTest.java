package com.eventmanagement.event_management_system.serviceTest;

import com.eventmanagement.event_management_system.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.eventmanagement.event_management_system.dto.AdminDTO;
import com.eventmanagement.event_management_system.entity.Admin;
import com.eventmanagement.event_management_system.exception.DuplicateResourceException;
import com.eventmanagement.event_management_system.mapper.AdminMapper;
import com.eventmanagement.event_management_system.repository.AdminRepository;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private  AdminRepository adminRepository;
    @Mock
    private  AdminMapper adminMapper;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @InjectMocks
    private AdminService adminService;

    @Test
    void create_ShouldSaveAdmin_WhenEmailIsUnique() {
        AdminDTO inputDto = new AdminDTO();
        inputDto.setEmail("new@admin.com");
        inputDto.setPassword("123456");

        Admin entity = new Admin();
        entity.setEmail("new@admin.com");

        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(adminMapper.toEntity(any(AdminDTO.class))).thenReturn(entity);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(adminMapper.toDTO(any(Admin.class))).thenReturn(inputDto);

        AdminDTO result = adminService.create(inputDto);


        assertNotNull(result);
        verify(adminRepository, times(1)).save(any(Admin.class));
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void create_ShouldThrowException_WhenEmailAlreadyExists() {
        AdminDTO inputDto = new AdminDTO();
        inputDto.setEmail("existing@admin.com");

        when(adminRepository.findByEmail("existing@admin.com"))
                .thenReturn(Optional.of(new Admin()));

        assertThrows(DuplicateResourceException.class, () -> {
            adminService.create(inputDto);
        });

        verify(adminRepository, never()).save(any());
    }

}
