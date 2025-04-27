package com.mruruc.mail.sender.service;

import com.mruruc.mail.sender.exceptn.EmailServiceException;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

public abstract sealed class MailService
        permits SimpleMailService, HtmlMailService, AttachmentMailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    protected static final String CONTENT_TYPE_TEXT_HTML = "text/html; charset=utf-8";
    protected static final String MULTIPART_ALTERNATIVE = "alternative"; // Same content, different formats.(e.g Text + HTML versions)
    protected static final String MULTIPART_MIXED = "mixed";             // Different content combined (e.g Message + Attachment)   \
    protected static final String HEADER_IMPORTANCE = "Importance";
    protected static final String HEADER_X_PRIORITY = "X-Priority";
    protected static final String HEADER_X_CUSTOM = "X-Custom-Header";

    private final Session session;

    protected MailService(Session session) {
        this.session = Objects.requireNonNull(session, "Session cannot be null");
    }

    /**
     * Sends the prepared MimeMessage using Transport. Handles exceptions and logging.
     *
     * @param message The message to send.
     * @throws EmailServiceException if sending fails.
     */
    protected void sendMessage(MimeMessage message) {
        try {
            LOGGER.debug("Attempting to send email. Subject: '{}', Recipients: {}",
                    message.getSubject(), Arrays.toString(message.getAllRecipients())
            );
            Transport.send(message);
        } catch (MessagingException exception) {
            try {
                LOGGER.error("Failed to send email. Subject: '{}', Recipients: {}, Error: {}",
                        message.getSubject(), Arrays.toString(message.getAllRecipients()), exception.getMessage(), exception);
            } catch (MessagingException ignore) {
                LOGGER.error("Failed to send email. Error retrieving message details for logging.", exception);
            }
            throw new EmailServiceException("Failed to send email via transport.", exception);
        }
    }

    public Session getSession() {
        return session;
    }
}
