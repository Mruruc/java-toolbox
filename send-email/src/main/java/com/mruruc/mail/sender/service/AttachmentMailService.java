package com.mruruc.mail.sender.service;

import com.mruruc.mail.sender.exceptn.EmailServiceException;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class AttachmentMailService extends MailService {
    private final Logger LOGGER = LoggerFactory.getLogger(AttachmentMailService.class);

    public AttachmentMailService(Session session) {
        super(session);
    }


    public void sendEmailWithAttachment(String to, String subject,
                                        String htmlContent, File attachment) {
        Objects.requireNonNull(to, "Recipient email address cannot be null");
        Objects.requireNonNull(subject, "Email subject cannot be null");
        Objects.requireNonNull(htmlContent, "Email content cannot be null");
        Objects.requireNonNull(attachment, "Attachment cannot be null");

        try {
            var session = this.getSession();

            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            // Create the message body
            Multipart multipart = new MimeMultipart(MULTIPART_MIXED);


            // HTML part
            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, CONTENT_TYPE_TEXT_HTML);

            multipart.addBodyPart(htmlPart);

            // Add the attachment part
            if (attachment.exists()) {
                var attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(attachment);
                multipart.addBodyPart(attachmentPart);
            }

            // Set the multipart content to the message
            message.setContent(multipart);

            // Send the email
            this.sendMessage(message);

            LOGGER.info("Email with attachment sent successfully.");
        } catch (MessagingException | IOException exception) {
            throw new EmailServiceException(exception.getMessage());
        }
    }


}
