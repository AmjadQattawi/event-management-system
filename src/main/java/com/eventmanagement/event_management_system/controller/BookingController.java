package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.BookingDTO;
import com.eventmanagement.event_management_system.interfaceService.IBookingService;
import com.eventmanagement.event_management_system.searchCriteria.BookingSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Booking")
@RequiredArgsConstructor
public class BookingController  extends BaseController<BookingDTO,Long> {

    private final IBookingService iBookingService;

    @Override
    protected BaseService<BookingDTO, Long> getService() {
        return iBookingService;
    }

    @PreAuthorize("hasRole('ATTENDEE')")
    @PostMapping("/create")
    public ResponseEntity<BookingDTO> create(@Valid @RequestBody BookingDTO bookingDTO) {
        return new ResponseEntity<>(iBookingService.create(bookingDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ATTENDEE','ORGANIZER')")
    @PutMapping("/update")
    public ResponseEntity<BookingDTO> update(
            @RequestParam Long id,
            @Valid @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(iBookingService.update(id,bookingDTO));
    }

    @PreAuthorize("hasAnyRole('ATTENDEE','ORGANIZER')")
    @GetMapping("/search")
    public Page<BookingDTO> search(
            BookingSearchCriteria bookingSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){
        return iBookingService.search(bookingSearchCriteria,page,size);
    }


    }
