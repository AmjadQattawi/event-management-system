package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.AdminDTO;
import com.eventmanagement.event_management_system.interfaceService.IAdminService;
import com.eventmanagement.event_management_system.searchCriteria.AdminSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Admin")
@RequiredArgsConstructor
public class AdminController extends BaseController<AdminDTO,Long> {

    private final IAdminService iAdminService;

    @Override
    protected BaseService<AdminDTO, Long> getService() {
        return iAdminService;
    }

    @Override
    @GetMapping("/findById")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDTO> findById(Long id) {
        return ResponseEntity.ok(getService().findById(id));
    }
    @Override
    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminDTO>> findAll() {
        return ResponseEntity.ok(getService().findAll());
    }
    @Override
    @GetMapping("/findByPage")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminDTO>> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return ResponseEntity.ok(getService().findByPage(page,size,sortBy,direction));
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDTO> create(@Valid @RequestBody AdminDTO adminDTO) {
        return new ResponseEntity<>(iAdminService.create(adminDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDTO> update(
            @RequestParam Long id,
            @Valid @RequestBody AdminDTO adminDTO) {
        return ResponseEntity.ok(iAdminService.update(id, adminDTO));
    }

    @PutMapping("/updateMyProfile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDTO> updateMyProfile(
            @Valid @RequestBody AdminDTO adminDTO) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(iAdminService.updateByEmail(email, adminDTO));
    }
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminDTO>> search(
            AdminSearchCriteria adminSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){
        return ResponseEntity.ok(iAdminService.search(adminSearchCriteria,page,size));

    }


}
