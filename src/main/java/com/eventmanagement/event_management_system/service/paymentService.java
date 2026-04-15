package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.entity.Payment;
import com.eventmanagement.event_management_system.enums.BookingStatus;
import com.eventmanagement.event_management_system.enums.EventStatus;
import com.eventmanagement.event_management_system.enums.PaymentStatus;
import com.eventmanagement.event_management_system.exception.IllegalStatusException;
import com.eventmanagement.event_management_system.exception.PaymentMismatchException;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.interfaceService.IPaymentService;
import com.eventmanagement.event_management_system.mapper.PaymentMapper;
import com.eventmanagement.event_management_system.repository.BookingRepository;
import com.eventmanagement.event_management_system.repository.PaymentRepository;
import com.eventmanagement.event_management_system.searchCriteria.PaymentSearchCriteria;
import com.eventmanagement.event_management_system.specification.PaymentSpecification;
import com.eventmanagement.event_management_system.validator.PaymentNotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class paymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentNotificationService paymentNotificationService;

    @Override
    @Transactional
    public PaymentDTO create(PaymentDTO paymentDTO) {
        Booking booking=bookingRepository.findById(paymentDTO.getBookingId())
           .orElseThrow(()->new ResourceNotFoundException("Booking not found with id: "+ paymentDTO.getBookingId()));
        Payment payment=paymentMapper.toEntity(paymentDTO);

             if (booking.getBookingStatus()== BookingStatus.CANCELLED){
            throw new IllegalStatusException("INVALID_STATE (Booking CANCELLED)");
        }if (booking.getEvent().getEventStatus()== EventStatus.CANCELLED){
            throw new IllegalStatusException("INVALID_STATE (Event CANCELLED)");
        }
        if (!payment.getAmount().equals(booking.getTotalPrice())){
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }else {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            booking.setBookingStatus(BookingStatus.CONFIRMED);
        }
        int pointsToAdd = booking.getNumberOfTickets() * 10; // 10 points per ticket
        int currentPoints = booking.getAttendee().getRewardPoints() != null ?
                booking.getAttendee().getRewardPoints() : 0;
        booking.getAttendee().setRewardPoints(currentPoints + pointsToAdd);
        payment.setBooking(booking);
        Payment saved=paymentRepository.save(payment);
        paymentNotificationService.sendEmail(paymentDTO);
        return paymentMapper.toDTO(saved);

    }

    @Override
    public PaymentDTO findById(Long id) {
        Payment payment=paymentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found with id "+ id));
                return paymentMapper.toDTO(payment);
    }

    @Override
    public List<PaymentDTO> findAll() {
        List<Payment> payments=paymentRepository.findAll();
        return paymentMapper.toDTO(payments);
    }

    @Override
    @Transactional
    public PaymentDTO update(Long id, PaymentDTO paymentDTO) {
        Payment payment=paymentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found with id "+ id));
        Booking booking=bookingRepository.findById(paymentDTO.getBookingId())
                .orElseThrow(()->new ResourceNotFoundException("Booking not found with id: "+ paymentDTO.getBookingId()));
        if (!paymentDTO.getAmount().equals(booking.getTotalPrice())){
            payment.setPaymentStatus(PaymentStatus.FAILED);
            throw new PaymentMismatchException("amount does not equal totalPrice");
        }
        if (booking.getBookingStatus()!=BookingStatus.PENDING)
            throw new IllegalStatusException("You cannot edit if BookingStatus is not PENDING");
        else
            booking.setBookingStatus(BookingStatus.CONFIRMED);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setBooking(booking);
        Payment saved=paymentRepository.save(payment);
        return paymentMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalStatusException("Cannot delete a COMPLETED payment! It is part of the financial history.");
        }
        paymentRepository.delete(payment);
    }

    @Override
    public Page<PaymentDTO> findByPage(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Payment> attendeePage=paymentRepository.findAll(pageable);
        return attendeePage.map(paymentMapper::toDTO);
    }

    @Override
    public Page<PaymentDTO> search(PaymentSearchCriteria paymentSearchCriteria, int page, int size) {
        Pageable pageable=PageRequest.of(page,size);
        Specification<Payment> specification= PaymentSpecification.search(paymentSearchCriteria);
        Page<Payment> page1=paymentRepository.findAll(specification,pageable);
        return page1.map(paymentMapper::toDTO);
    }


}
