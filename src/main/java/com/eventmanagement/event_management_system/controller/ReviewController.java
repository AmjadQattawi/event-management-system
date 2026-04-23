package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.dto.ReviewDTO;
import com.eventmanagement.event_management_system.interfaceService.IReviewService;
import com.eventmanagement.event_management_system.searchCriteria.ReviewSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Review")
@RequiredArgsConstructor
public class ReviewController extends BaseController<ReviewDTO,Long> {

    private final IReviewService iReviewService;

    @Override
    protected BaseService<ReviewDTO, Long> getService() {
        return iReviewService;
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ATTENDEE')")
    public ResponseEntity<ReviewDTO> create(@Valid @RequestBody ReviewDTO reviewDTO) {
        return new ResponseEntity<>(iReviewService.create(reviewDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ATTENDEE','ADMIN')")
    public ResponseEntity<ReviewDTO> update(
            @RequestParam Long id,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(iReviewService.update(id,reviewDTO));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public Page<ReviewDTO> search(
            ReviewSearchCriteria reviewSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return iReviewService.search(reviewSearchCriteria,page,size);
    }

    }
