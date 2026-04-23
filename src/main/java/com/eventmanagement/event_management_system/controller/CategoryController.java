package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.CategoryDTO;
import com.eventmanagement.event_management_system.interfaceService.ICategoryService;
import com.eventmanagement.event_management_system.searchCriteria.CategorySearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Category")
@RequiredArgsConstructor
public class CategoryController extends BaseController<CategoryDTO,Long>{

    private final ICategoryService iCategoryService;

    @Override
    protected BaseService<CategoryDTO, Long> getService() {
        return iCategoryService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(iCategoryService.create(categoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> update(
            @RequestParam Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(iCategoryService.update(id,categoryDTO));
    }


    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public Page<CategoryDTO> search(
            CategorySearchCriteria categorySearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return iCategoryService.search(categorySearchCriteria,page,size);
    }

    }

