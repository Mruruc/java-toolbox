package com.mruruc.mail.sender.service;

import com.mruruc.mail.sender.exceptn.EmailServiceException;
import com.mruruc.mail.sender.template.EmailTemplate;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.Objects;

import static jakarta.mail.Message.RecipientType.TO;

public final class HtmlMailService extends MailService {
    private final TemplateEngine templateEngine;

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlMailService.class);

    public HtmlMailService(Session session, TemplateEngine templateEngine) {
        super(session);
        this.templateEngine = templateEngine;
    }



    public void sendMultipartMail(String to, String subject, String textContent, String htmlContent) {
        Objects.requireNonNull(to, "Recipient 'to' address cannot be null");
        Objects.requireNonNull(subject, "Email subject cannot be null");
        Objects.requireNonNull(textContent, "Text content cannot be null");
        Objects.requireNonNull(htmlContent, "HTML content cannot be null");

        try {
            var session = this.getSession();

            MimeMessage message = new MimeMessage(session);
            message.addRecipient(TO, new InternetAddress(to));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart(MULTIPART_ALTERNATIVE);

            // Create the plain text part
            BodyPart textPart = new MimeBodyPart();
            textPart.setText(textContent);

            // Create the HTML part
            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, CONTENT_TYPE_TEXT_HTML);

            // Add the parts to the multipart
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(htmlPart);

            // Set the multipart content to the message
            message.setContent(multipart);

            // Send the email
            this.sendMessage(message);

            LOGGER.info("Multipart Email sent successfully.");
        } catch (MessagingException exception) {
            throw new EmailServiceException("Failed to send multipart email.", exception);
        }
    }

    public void accountActivationEmail(String to, EmailTemplate template, Map<String, Object> templateVars) {
        Objects.requireNonNull(to, "Recipient 'to' address cannot be null");
        Objects.requireNonNull(template, "EmailTemplate cannot be null");
        Objects.requireNonNull(templateVars, "Template variables cannot be null");

        try {
            final String subject = "Account activation";
            final String htmlContent =
                    this.processTemplate(template.getName(), templateVars);

            Session session = getSession();

            var message = new MimeMessage(session);
            message.setSubject(subject);
            message.addRecipient(TO, new InternetAddress(to));
            message.setContent(htmlContent, CONTENT_TYPE_TEXT_HTML);

            this.sendMessage(message);
            LOGGER.info("{} email sent successfully to {}", subject, to);
        } catch (MessagingException exception) {
            throw new EmailServiceException(exception.getMessage(), exception);
        }
    }


    /**
     * Sends an HTML email using the provided parameters.
     *
     * @param from        Sender's email address
     * @param to          Recipient's email address
     * @param subject     Subject of the email
     * @param htmlContent HTML content of the email
     */
    public void sendHtmlEmail(String from, String to, String subject, String htmlContent) {
        Objects.requireNonNull(from, "Sender 'from' address cannot be null");
        Objects.requireNonNull(to, "Recipient 'to' address cannot be null");
        Objects.requireNonNull(subject, "Email subject cannot be null");
        Objects.requireNonNull(htmlContent, "HTML content cannot be null");

        try {
            Session session = getSession();
            var message = new MimeMessage(session);
            message.setFrom(from);
            message.addRecipient(TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(htmlContent, CONTENT_TYPE_TEXT_HTML);

            this.sendMessage(message);
            LOGGER.info("HTML Email sent to {} successfully.", to);
        } catch (MessagingException exception) {
            throw new EmailServiceException(exception.getMessage(), exception);
        }
    }


    /**
     * Processes a Thymeleaf template.
     *
     * @param templateName Template file name (without path).
     * @param variables    Map of variables for the template context.
     * @return Processed HTML content as a String.
     */
    private String processTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }

}
