package com.eventmanagement.event_management_system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex){
    return ResponseEntity.status(404).body(ex.getMessage());
}
@ExceptionHandler(InvalidEventDataException.class)
    public ResponseEntity<String> handleInvalidEventData(InvalidEventDataException ex){
    return ResponseEntity.status(404).body(ex.getMessage());
}

@ExceptionHandler(InsufficientCapacityException.class)
    public ResponseEntity<String> handleInsufficientCapacity(InsufficientCapacityException ex){
    return ResponseEntity.status(409).body(ex.getMessage());
}

@ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateBooking(DuplicateResourceException ex){
    return ResponseEntity.status(409).body(ex.getMessage());
}

@ExceptionHandler(PaymentIncompleteException.class)
    public ResponseEntity<String> handlePaymentError(PaymentIncompleteException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
}

@ExceptionHandler(PaymentMismatchException.class)
    public ResponseEntity<String> handlePaymentMismatch(PaymentMismatchException ex){
    return ResponseEntity.status(400).body(ex.getMessage());
}

@ExceptionHandler(IllegalStatusException.class)
    public ResponseEntity<String> handleIllegalBookingStatus(IllegalStatusException ex){
    return ResponseEntity.status(400).body(ex.getMessage());
}



}
