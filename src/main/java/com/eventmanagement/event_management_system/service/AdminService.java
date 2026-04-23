package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.AdminDTO;
import com.eventmanagement.event_management_system.entity.Admin;
import com.eventmanagement.event_management_system.enums.Role;
import com.eventmanagement.event_management_system.enums.UserStatus;
import com.eventmanagement.event_management_system.exception.DuplicateResourceException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.interfaceService.IAdminService;
import com.eventmanagement.event_management_system.mapper.AdminMapper;
import com.eventmanagement.event_management_system.repository.AdminRepository;
import com.eventmanagement.event_management_system.searchCriteria.AdminSearchCriteria;
import com.eventmanagement.event_management_system.specification.AdminSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {


    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;

    @Override
    @Transactional
    public AdminDTO create(AdminDTO adminDTO) {
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Sorry, this email is already registered in the system!");
        }
        Admin admin = adminMapper.toEntity(adminDTO);
        admin.setRole(Role.ADMIN);
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        if (admin.getUserStatus() == null) {
            admin.setUserStatus(UserStatus.ACTIVE);
        }
        adminRepository.save(admin);
        return adminMapper.toDTO(admin);
    }

    @Override
    public AdminDTO findById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("admin not found with id:" + id));
        return adminMapper.toDTO(admin);
    }

    @Override
    public List<AdminDTO> findAll() {
        List<Admin> admins = adminRepository.findAll();
        return adminMapper.toDTO(admins);
    }

    @Override
    @Transactional
    public AdminDTO update(Long id, AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin not found with id: " + id));
        adminMapper.updateAdminFromDto(adminDTO, admin);
        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        }
        Admin saved = adminRepository.save(admin);
        return adminMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public AdminDTO updateByEmail(String email, AdminDTO adminDTO) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admin not found with email: " + email));
        adminMapper.updateAdminFromDto(adminDTO, admin);
        if (adminDTO.getPassword() != null && !adminDTO.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        }
        Admin saved = adminRepository.save(admin);
        return adminMapper.toDTO(saved);
    }

    @Override
    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Attendee not found ");
        }
        adminRepository.deleteById(id);
    }

    @Override
    public Page<AdminDTO> findByPage(int page,int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Admin> attendeePage = adminRepository.findAll(pageable);
        return attendeePage.map(adminMapper::toDTO);
    }


    @Override
    public Page<AdminDTO> search(AdminSearchCriteria adminSearchCriteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Admin> specification = AdminSpecification.search(adminSearchCriteria);
        Page<Admin> page1 = adminRepository.findAll(specification, pageable);
        return page1.map(adminMapper::toDTO);
    }



}



