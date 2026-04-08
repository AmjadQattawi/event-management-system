package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.entity.Organizer;
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
import com.eventmanagement.event_management_system.searchCriteria.EventSearchCriteria;
import com.eventmanagement.event_management_system.searchCriteria.PaymentSearchCriteria;
import com.eventmanagement.event_management_system.specification.EventSpecification;
import com.eventmanagement.event_management_system.specification.OrganizerSpecification;
import com.eventmanagement.event_management_system.specification.PaymentSpecification;
import com.eventmanagement.event_management_system.specification.ReviewSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class paymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${BREVO_SENDER_EMAIL}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;



    @Override
    @Transactional
    public PaymentDTO create(PaymentDTO paymentDTO) {
        Booking booking=bookingRepository.findById(paymentDTO.getBookingId())
           .orElseThrow(()->new ResourceNotFoundException("Booking not found with id: "+ paymentDTO.getBookingId()));
        Payment payment=paymentMapper.toEntity(paymentDTO);
         if (payment.getAmount()!=(booking.getTotalPrice())){
            payment.setPaymentStatus(PaymentStatus.FAILED);
         }
        if (booking.getBookingStatus()== BookingStatus.CANCELLED){
            payment.setPaymentStatus(PaymentStatus.FAILED);
            throw new IllegalStatusException("INVALID_STATE (Booking CANCELLED)");
        }if (booking.getEvent().getEventStatus()== EventStatus.CANCELLED){
            payment.setPaymentStatus(PaymentStatus.FAILED);
            throw new IllegalStatusException("INVALID_STATE (Event CANCELLED)");
        }
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            booking.setBookingStatus(BookingStatus.CONFIRMED);
        int pointsToAdd = booking.getNumberOfTickets() * 10; // 10 points per ticket
        int currentPoints = booking.getAttendee().getRewardPoints() != null ?
                booking.getAttendee().getRewardPoints() : 0;
        booking.getAttendee().setRewardPoints(currentPoints + pointsToAdd);
        payment.setBooking(booking);
        Payment saved=paymentRepository.save(payment);
        sendEmail(paymentDTO);
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
        Payment payment=paymentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found with id: "+id));
        if (payment.getPaymentStatus()==PaymentStatus.FAILED || payment.getPaymentStatus()== PaymentStatus.REFUNDED){
            Booking booking=payment.getBooking();
            if (booking!=null){
                booking.setPayment(null);
            }
        }
        if (payment.getPaymentStatus()==PaymentStatus.COMPLETED)
            throw new IllegalStatusException("cant delete payment !! It has an important financial history");
        paymentRepository.deleteById(id);
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




    public void sendEmail(PaymentDTO paymentDTO){
        String url = "https://api.brevo.com/v3/smtp/email";
        Booking booking = bookingRepository.findById(paymentDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        String toEmail=booking.getAttendee().getEmail();
        String toName=booking.getAttendee().getName();

        HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.set("api-key",apiKey.trim());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("name", senderName, "email", senderEmail));
        body.put("to", List.of(Map.of("email", toEmail)));
        body.put("subject", "Payment Successful");
        body.put("htmlContent", "<h1>Payment completed!</h1>");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, httpHeaders);
        restTemplate.postForObject(url,request,String.class);
    }

}
