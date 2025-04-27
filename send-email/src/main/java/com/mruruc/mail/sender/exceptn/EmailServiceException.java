package com.mruruc.mail.sender.exceptn;

/**
 * Custom unchecked exception for email service errors.
 */
public class EmailServiceException extends RuntimeException {
    public EmailServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailServiceException(String message) {
        super(message);
    }
}
