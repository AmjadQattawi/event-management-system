package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.entity.Attendee;
import com.eventmanagement.event_management_system.enums.Role;
import com.eventmanagement.event_management_system.enums.UserStatus;
import com.eventmanagement.event_management_system.exception.DuplicateResourceException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.interfaceService.IAttendeeService;
import com.eventmanagement.event_management_system.mapper.AttendeeMapper;
import com.eventmanagement.event_management_system.repository.AttendeeRepository;
import com.eventmanagement.event_management_system.searchCriteria.AttendeeSearshCriteria;
import com.eventmanagement.event_management_system.specification.AttendeeSpecification;
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
public class AttendeeService implements IAttendeeService {


    private final AttendeeRepository attendeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttendeeMapper attendeeMapper;

    @Override
    @Transactional
    public AttendeeDTO create(AttendeeDTO attendeeDTO) {
        if (attendeeRepository.findByEmail(attendeeDTO.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Sorry, this email is already registered in the system!");
        }
        Attendee attendee=attendeeMapper.toEntity(attendeeDTO);
        attendee.setRole(Role.ATTENDEE);
        attendee.setPassword(passwordEncoder.encode(attendeeDTO.getPassword()));
        if (attendee.getUserStatus() == null) {
            attendee.setUserStatus(UserStatus.ACTIVE);
        }
        attendee.setRewardPoints(10);
        Attendee saved=attendeeRepository.save(attendee);
        return attendeeMapper.toDTO(saved);
    }


    @Override
    public AttendeeDTO findById(Long id) {
        Attendee attendee=attendeeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Attendee not found with id:"+id));
        return attendeeMapper.toDTO(attendee);
    }

    @Override
    public List<AttendeeDTO> findAll() {
        List<Attendee> attendees=attendeeRepository.findAll();
        return attendeeMapper.toDTO(attendees);
    }

    @Override
    @Transactional
    public AttendeeDTO update(Long id, AttendeeDTO attendeeDTO) {
        Attendee attendee=attendeeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Attendee not found with id:"+id));
        attendeeMapper.updateAttendeeFromDto(attendeeDTO,attendee);
        if (attendeeDTO.getPassword() != null && !attendeeDTO.getPassword().isBlank()) {
            attendee.setPassword(passwordEncoder.encode(attendeeDTO.getPassword()));
        }
        Attendee saved=attendeeRepository.save(attendee);
        return attendeeMapper.toDTO(saved);
    }


    @Override
    @Transactional
    public AttendeeDTO updateByEmail(String email, AttendeeDTO attendeeDTO) {
        Attendee attendee = attendeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Attendee not found"));
        attendeeMapper.updateAttendeeFromDto(attendeeDTO,attendee);
        if (attendeeDTO.getPassword() != null && !attendeeDTO.getPassword().isBlank()) {
            attendee.setPassword(passwordEncoder.encode(attendeeDTO.getPassword()));
        }
        return attendeeMapper.toDTO(attendeeRepository.save(attendee));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        Attendee attendee=attendeeRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Attendee not found with id:"+id));
        attendeeRepository.delete(attendee);
    }


    public Page<AttendeeDTO> findByPage(int page, int size, String sortBy, String direction){
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Attendee> attendeePage=attendeeRepository.findAll(pageable);
        return attendeePage.map(attendeeMapper::toDTO);
    }


    @Override
    public Page<AttendeeDTO> search(AttendeeSearshCriteria attendeeSearshCriteria,int page,int size) {
        Pageable pageable=PageRequest.of(page,size);
        Specification<Attendee> specification=AttendeeSpecification.search(attendeeSearshCriteria);
        Page<Attendee> page1=attendeeRepository.findAll(specification,pageable);
        return page1.map(attendeeMapper::toDTO);
    }



}
