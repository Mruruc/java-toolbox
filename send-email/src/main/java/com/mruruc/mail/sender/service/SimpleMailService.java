package com.mruruc.mail.sender.service;

import com.mruruc.mail.sender.exceptn.EmailServiceException;
import com.mruruc.mail.sender.template.Importance;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jakarta.mail.Message.RecipientType.TO;

public final class SimpleMailService extends MailService {
    private final Logger LOGGER = LoggerFactory.getLogger(SimpleMailService.class);

    public SimpleMailService(Session session) {
        super(session);
    }

    public void sendSimpleTextMail(String from, String subject,
                                   String to, String text, Importance importance) {
        try {
            Session session = getSession();

            MimeMessage message = new MimeMessage(session);

            // message headers
            message.setFrom(new InternetAddress(from));
            message.addRecipient(TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setHeader(HEADER_IMPORTANCE, importance.name());
            message.setHeader(HEADER_X_CUSTOM, "From Mr_Uruc");

            // message body
            message.setText(text);

            this.sendMessage(message);

            LOGGER.info("Email {}, sent to {} successfully.", subject, to);
        } catch (MessagingException exception) {
            throw new EmailServiceException(exception.getMessage(), exception);
        }
    }
}
