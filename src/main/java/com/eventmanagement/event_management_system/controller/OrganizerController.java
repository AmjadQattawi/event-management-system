package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.dto.OrganizerDTO;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.interfaceService.IEventService;
import com.eventmanagement.event_management_system.interfaceService.IOrganizerService;
import com.eventmanagement.event_management_system.searchCriteria.OrganizerSearchCriteria;
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
@RequestMapping("/Organizer")
@RequiredArgsConstructor
public class OrganizerController extends BaseController<OrganizerDTO,Long>{

    private final IOrganizerService iOrganizerService;


    @Override
    protected BaseService<OrganizerDTO, Long> getService() {
        return iOrganizerService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrganizerDTO> create(@Valid @RequestBody OrganizerDTO organizerDTO) {
        return new ResponseEntity<>(iOrganizerService.create(organizerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<OrganizerDTO> update(Long id, @Valid @RequestBody OrganizerDTO organizerDTO) {
        return ResponseEntity.ok(iOrganizerService.update(id,organizerDTO));
    }

    @GetMapping("/search")
    public Page<OrganizerDTO> search(
            OrganizerSearchCriteria organizerSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return iOrganizerService.search(organizerSearchCriteria,page,size);
    }

}
