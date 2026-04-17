package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.OrganizerDTO;
import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.interfaceService.IOrganizerService;
import com.eventmanagement.event_management_system.interfaceService.IPaymentService;
import com.eventmanagement.event_management_system.searchCriteria.PaymentSearchCriteria;
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
@RequestMapping("/Payment")
@RequiredArgsConstructor
public class PaymentController extends BaseController<PaymentDTO,Long> {

    private IPaymentService iPaymentService;

    @Override
    protected BaseService<PaymentDTO, Long> getService() {
        return iPaymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentDTO> create(@Valid @RequestBody PaymentDTO paymentDTO) {
        return new ResponseEntity<>(iPaymentService.create(paymentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<PaymentDTO> update(Long id, @Valid @RequestBody PaymentDTO paymentDTO) {
        return ResponseEntity.ok(iPaymentService.update(id,paymentDTO));
    }

    @GetMapping("/search")
    public Page<PaymentDTO> search(
            PaymentSearchCriteria paymentSearchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return iPaymentService.search(paymentSearchCriteria,page,size);
    }


    }
