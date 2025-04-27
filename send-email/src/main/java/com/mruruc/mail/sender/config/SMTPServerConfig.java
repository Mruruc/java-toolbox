package com.mruruc.mail.sender.config;

import com.mruruc.mail.sender.exceptn.SMTPConfigException;
import jakarta.mail.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SMTPServerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMTPServerConfig.class);
    private static final Properties mailProps = new Properties();
    private static TemplateEngine templateEngine;

    private SMTPServerConfig() {
    }

    static {
        try (InputStream input = SMTPServerConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                LOGGER.error("ERROR: Unable to find application.properties in classpath.");
                throw new SMTPConfigException("application.properties not found, application cannot start.");
            }
            mailProps.load(input);
            LOGGER.info("INFO: SMTP server properties loaded successfully.");
        } catch (IOException exception) {
            throw new SMTPConfigException("Failed to load application.properties.", exception);
        }
    }

    public static Session getSession() {
        final String smtpUsername = mailProps.getProperty("smtp.username");
        final String smtpPassword = mailProps.getProperty("smtp.password");

        var authenticator = new AuthenticatorImp(
                smtpUsername, smtpPassword
        );
        return Session.getInstance(mailProps, authenticator);
    }


    public static TemplateEngine getTemplateEngine() {
        if (templateEngine == null) {
            var templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("/templates/email/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCharacterEncoding("UTF-8");
            templateResolver.setCacheable(false);

            templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);
        }

        return templateEngine;
    }

}
