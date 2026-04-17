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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Admin")
@RequiredArgsConstructor
public class AdminController extends BaseController<AdminDTO,Long> {

    private final IAdminService iAdminService;

    @Override
    protected BaseService<AdminDTO, Long> getService() {
        return iAdminService;
    }

    @PostMapping("/create")
    public ResponseEntity<AdminDTO> create(@Valid @RequestBody AdminDTO adminDTO) {
        return new ResponseEntity<>(iAdminService.create(adminDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<AdminDTO> update(Long id, @Valid @RequestBody AdminDTO adminDTO) {
        return ResponseEntity.ok(iAdminService.update(id,adminDTO));
    }
    @GetMapping("/search")
    public ResponseEntity<Page<AdminDTO>> search(
            AdminSearchCriteria adminSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){
        return ResponseEntity.ok(iAdminService.search(adminSearchCriteria,page,size));

    }


}
