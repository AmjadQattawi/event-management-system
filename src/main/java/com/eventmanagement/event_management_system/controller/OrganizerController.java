package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.AttendeeDTO;
import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.dto.OrganizerDTO;
import com.eventmanagement.event_management_system.entity.Event;
import com.eventmanagement.event_management_system.interfaceService.IEventService;
import com.eventmanagement.event_management_system.interfaceService.IOrganizerService;
import com.eventmanagement.event_management_system.searchCriteria.OrganizerSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.imap.protocol.ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<List<OrganizerDTO>> findAll() {
        return ResponseEntity.ok(getService().findAll());
    }

    @GetMapping("/findById")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<OrganizerDTO> findById( Long id) {
        return ResponseEntity.ok(getService().findById(id));
    }

    @GetMapping("/findByPage")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public ResponseEntity<Page<OrganizerDTO>> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return ResponseEntity.ok(getService().findByPage(page,size,sortBy,direction));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<OrganizerDTO> create(@Valid @RequestBody OrganizerDTO organizerDTO) {
        return new ResponseEntity<>(iOrganizerService.create(organizerDTO), HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<OrganizerDTO> update(
            @RequestParam Long id,
            @Valid @RequestBody OrganizerDTO organizerDTO) {
        return ResponseEntity.ok(iOrganizerService.update(id,organizerDTO));
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/updateMyProfile")
    public ResponseEntity<OrganizerDTO> updateMyProfile(
            @Valid @RequestBody OrganizerDTO organizerDTO) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(iOrganizerService.updateByEmail(email, organizerDTO));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
    public Page<OrganizerDTO> search(
            OrganizerSearchCriteria organizerSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return iOrganizerService.search(organizerSearchCriteria,page,size);
    }



}
