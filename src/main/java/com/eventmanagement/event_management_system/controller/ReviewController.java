package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.dto.ReviewDTO;
import com.eventmanagement.event_management_system.interfaceService.IReviewService;
import com.eventmanagement.event_management_system.searchCriteria.ReviewSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Review")
public class ReviewController extends BaseController<ReviewDTO,Long> {

    @Autowired
    private IReviewService iReviewService;

    @Override
    protected BaseService<ReviewDTO, Long> getService() {
        return iReviewService;
    }
    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> create(@Valid @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(iReviewService.create(reviewDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ReviewDTO> update(Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(iReviewService.update(id,reviewDTO));
    }

    @GetMapping("/search")
    public Page<ReviewDTO> search(
            ReviewSearchCriteria reviewSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return iReviewService.search(reviewSearchCriteria,page,size);
    }

    }
