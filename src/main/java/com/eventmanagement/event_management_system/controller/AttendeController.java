package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.interfaceService.IAttendeeService;
import com.eventmanagement.event_management_system.searchCriteria.AttendeeSearshCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Attendee")
@RequiredArgsConstructor
public class AttendeController extends BaseController<AttendeeDTO,Long> {

    private final IAttendeeService iAttendeeService;

    @Override
    protected BaseService<AttendeeDTO, Long> getService() {
        return iAttendeeService;
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendeeDTO> create(@Valid @RequestBody AttendeeDTO attendeeDTO) {
        return new ResponseEntity<>(iAttendeeService.create(attendeeDTO),HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendeeDTO> update(
            @RequestParam Long id,
            @Valid @RequestBody AttendeeDTO attendeeDTO) {
        return ResponseEntity.ok(iAttendeeService.update(id, attendeeDTO));
    }

    @PutMapping("/updateMyProfile")
    @PreAuthorize("hasRole('ATTENDEE')")
    public ResponseEntity<AttendeeDTO> updateMyProfile(
            @Valid @RequestBody AttendeeDTO dto) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(iAttendeeService.updateByEmail(email, dto));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AttendeeDTO> search(
            AttendeeSearshCriteria attendeeSearshCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){
        return iAttendeeService.search(attendeeSearshCriteria,page,size);
    }

    }

