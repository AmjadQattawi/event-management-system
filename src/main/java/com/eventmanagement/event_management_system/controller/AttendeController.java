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
    public ResponseEntity<AttendeeDTO> create(@Valid @RequestBody AttendeeDTO attendeeDTO) {
        return new ResponseEntity<>(iAttendeeService.create(attendeeDTO),HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<AttendeeDTO> update(Long id, @Valid @RequestBody AttendeeDTO attendeeDTO) {
        return ResponseEntity.ok(iAttendeeService.update(id,attendeeDTO));
    }

    @GetMapping("/search")
    public Page<AttendeeDTO> search(
            AttendeeSearshCriteria attendeeSearshCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){
        return iAttendeeService.search(attendeeSearshCriteria,page,size);
    }

    }

