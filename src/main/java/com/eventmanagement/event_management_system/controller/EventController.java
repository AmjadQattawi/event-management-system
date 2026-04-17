package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.dto.EventWithOrganizersDTO;
import com.eventmanagement.event_management_system.interfaceService.IEventService;
import com.eventmanagement.event_management_system.searchCriteria.EventSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDTO) {
        return new ResponseEntity<>(iEventService.create(eventDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<EventDTO> update(Long id, @Valid @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(iEventService.update(id,eventDTO));
    }

    @GetMapping("/findEventWithOrganizersDTO")
    public ResponseEntity<List<EventWithOrganizersDTO>> findEventWithOrganizersDTO(){
        return ResponseEntity.ok(iEventService.findEventWithOrganizers());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EventDTO>> search(
            EventSearchCriteria eventSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(iEventService.search(eventSearchCriteria,page,size));

    }




    }
