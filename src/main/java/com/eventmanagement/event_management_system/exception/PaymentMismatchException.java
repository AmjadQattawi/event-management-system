package com.eventmanagement.event_management_system.exception;

public class PaymentMismatchException extends RuntimeException {
    public PaymentMismatchException(String message) {
        super(message);
    }
}
