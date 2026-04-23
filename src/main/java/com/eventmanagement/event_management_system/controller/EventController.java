package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.interfaceService.IEventService;
import com.eventmanagement.event_management_system.searchCriteria.EventSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Event")
@RequiredArgsConstructor
public class EventController extends BaseController<EventDTO,Long>{

    private final IEventService iEventService;

    @Override
    protected BaseService<EventDTO, Long> getService() {
        return iEventService;
    }


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDTO) {
        return new ResponseEntity<>(iEventService.create(eventDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<EventDTO> update(
            @RequestParam Long id,
            @Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(iEventService.update(id,eventDTO));
    }



    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<EventDTO>> search(
            EventSearchCriteria eventSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(iEventService.search(eventSearchCriteria,page,size));
    }


    }
