package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.AdminDTO;
import com.eventmanagement.event_management_system.entity.Admin;
import com.eventmanagement.event_management_system.enums.Role;
import com.eventmanagement.event_management_system.enums.UserStatus;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.interfaceService.IAdminService;
import com.eventmanagement.event_management_system.mapper.AdminMapper;
import com.eventmanagement.event_management_system.repository.AdminRepository;
import com.eventmanagement.event_management_system.searchCriteria.AdminSearchCriteria;
import com.eventmanagement.event_management_system.specification.AdminSpecification;
import jakarta.transaction.Transactional;
import org.hibernate.cache.cfg.internal.AbstractDomainDataCachingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements IAdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    @Transactional
    public AdminDTO create(AdminDTO adminDTO) {
        Admin admin = adminMapper.toEntity(adminDTO);
        if (admin.getUserStatus() == null) {
            admin.setUserStatus(UserStatus.ACTIVE);
        }
//        admin.setRole(Role.ADMIN);
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
                .orElseThrow(() -> new ResourceNotFoundException("admin not found with id:" + id));
        adminMapper.updateAttendeeFromDto(adminDTO, admin);
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



