package com.eventmanagement.event_management_system.validator;


import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.entity.Booking;
import com.eventmanagement.event_management_system.exception.ResourceNotFoundException;
import com.eventmanagement.event_management_system.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class PaymentNotificationService {

    private final BookingRepository bookingRepository;

    private final RestTemplate restTemplate;

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    @Value("${BREVO_SENDER_EMAIL}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    @Async("mailExecutor")
    public void sendEmail(PaymentDTO paymentDTO){
        String url = "https://api.brevo.com/v3/smtp/email";
        Booking booking = bookingRepository.findById(paymentDTO.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        String toEmail=booking.getAttendee().getEmail();

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
