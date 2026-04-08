package com.eventmanagement.event_management_system.exception;

public class PaymentIncompleteException extends RuntimeException {
    public PaymentIncompleteException(String message) {
        super(message);
    }
}
