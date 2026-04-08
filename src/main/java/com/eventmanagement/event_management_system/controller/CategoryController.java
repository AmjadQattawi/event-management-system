package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.dto.CategoryDTO;
import com.eventmanagement.event_management_system.dto.CategoryDTODetailed;
import com.eventmanagement.event_management_system.entity.Category;
import com.eventmanagement.event_management_system.interfaceService.ICategoryService;
import com.eventmanagement.event_management_system.searchCriteria.CategorySearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Category")
public class CategoryController extends BaseController<CategoryDTO,Long>{

    @Autowired
    private ICategoryService iCategoryService;


    @Override
    protected BaseService<CategoryDTO, Long> getService() {
        return iCategoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(iCategoryService.create(categoryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<CategoryDTO> update(Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(iCategoryService.update(id,categoryDTO));
    }


    @GetMapping("/findCategoryDetailed")
    public ResponseEntity<List<CategoryDTODetailed>> findCategoryDetailed() {
        return ResponseEntity.ok(iCategoryService.getCategoryDTODetailed());

    }

    @GetMapping("/search")
    public Page<CategoryDTO> search(
            CategorySearchCriteria categorySearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return iCategoryService.search(categorySearchCriteria,page,size);
    }

    }

