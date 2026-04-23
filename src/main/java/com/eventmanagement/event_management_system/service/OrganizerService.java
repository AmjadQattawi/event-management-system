package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.dto.OrganizerDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.entity.Organizer;
import com.eventmanagement.event_management_system.enums.Role;
import com.eventmanagement.event_management_system.enums.UserStatus;
import com.eventmanagement.event_management_system.exception.DuplicateResourceException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.interfaceService.IOrganizerService;
import com.eventmanagement.event_management_system.mapper.OrganizerMapper;
import com.eventmanagement.event_management_system.repository.OrganizerRepository;
import com.eventmanagement.event_management_system.searchCriteria.OrganizerSearchCriteria;
import com.eventmanagement.event_management_system.specification.OrganizerSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerService implements IOrganizerService {

    private final OrganizerRepository organizerRepository;
    private final OrganizerMapper organizerMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public OrganizerDTO create(OrganizerDTO organizerDTO) {
        if (organizerRepository.findByEmail(organizerDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Sorry, this email is already registered in the system!");
        }
        Organizer organizer=organizerMapper.toEntity(organizerDTO);
        organizer.setPassword(passwordEncoder.encode(organizerDTO.getPassword()));
        organizer.setRole(Role.ORGANIZER);
        if (organizer.getUserStatus() == null) {
            organizer.setUserStatus(UserStatus.ACTIVE);
        }
        Organizer saved=organizerRepository.save(organizer);
        return organizerMapper.toDTO(saved);
    }

    @Override
    public OrganizerDTO findById(Long id) {
        Organizer organizer=organizerRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Organizer not found with id :" + id));
        return organizerMapper.toDTO(organizer);
    }

    @Override
    public List<OrganizerDTO> findAll() {
        List<Organizer> organizers=organizerRepository.findAll();
        return organizerMapper.toDTO(organizers);
    }



    @Override
    @Transactional
    public OrganizerDTO update(@RequestParam Long id, OrganizerDTO organizerDTO) {
        Organizer organizer = organizerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organizer not found with id: " + id));
        if (organizerDTO.getPassword() != null && !organizerDTO.getPassword().isBlank()) {
            organizer.setPassword(passwordEncoder.encode(organizerDTO.getPassword()));
        }
        organizer.setUserStatus(organizerDTO.getUserStatus());
        organizer.setCompanyName(organizerDTO.getCompanyName());
        Organizer saved = organizerRepository.save(organizer);
        return organizerMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public OrganizerDTO updateByEmail(String email,OrganizerDTO organizerDTO){
        Organizer organizer=organizerRepository.findByEmail(email).
                orElseThrow(()->new ResourceNotFoundException("Organizer not found"));
        if (organizer.getPassword() != null && !organizerDTO.getPassword().isBlank()) {
            organizer.setPassword(passwordEncoder.encode(organizerDTO.getPassword()));
        }
        organizer.setCompanyName(organizerDTO.getCompanyName());
        return organizerMapper.toDTO(organizerRepository.save(organizer));
    }

    @Override
    public Page<OrganizerDTO> search(OrganizerSearchCriteria organizerSearchCriteria, int page, int size) {
        Pageable pageable=PageRequest.of(page,size);
        Specification<Organizer> specification= OrganizerSpecification.search(organizerSearchCriteria);
        Page<Organizer> page1=organizerRepository.findAll(specification,pageable);
        return page1.map(organizerMapper::toDTO);
    }

    @Override
    @Transactional
    public void delete(Long id){
        Organizer organizer=organizerRepository.findById(id)
                        .orElseThrow(()->new ResourceNotFoundException("Organizer not found with id: " + id));
        organizerRepository.delete(organizer);
    }

    @Override
    public Page<OrganizerDTO> findByPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Organizer> attendeePage=organizerRepository.findAll(pageable);
        return attendeePage.map(organizerMapper::toDTO);
    }


}
