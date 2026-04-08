package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.BookingDTO;

import com.eventmanagement.event_management_system.interfaceService.IBookingService;
import com.eventmanagement.event_management_system.searchCriteria.BookingSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Booking")
public class BookingController  extends BaseController<BookingDTO,Long> {

    @Autowired
    private IBookingService iBookingService;

    @Override
    protected BaseService<BookingDTO, Long> getService() {
        return iBookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<BookingDTO> create(@Valid @RequestBody BookingDTO bookingDTO) {
        return new ResponseEntity<>(iBookingService.create(bookingDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<BookingDTO> update(Long id, @Valid @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(iBookingService.update(id,bookingDTO));
    }
    @GetMapping("/search")
    public Page<BookingDTO> search(
            BookingSearchCriteria bookingSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size){
        return iBookingService.search(bookingSearchCriteria,page,size);
    }


    }
