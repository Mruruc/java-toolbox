package com.mruruc.mail.sender.exceptn;

/**
 * Custom exception class for handling SMTP configuration exception.
 */
public class SMTPConfigException extends RuntimeException{

    public SMTPConfigException(String message) {
        super(message);
    }

    public SMTPConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
