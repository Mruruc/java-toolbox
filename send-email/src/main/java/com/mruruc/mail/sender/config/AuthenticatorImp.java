package com.mruruc.mail.sender.config;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

public class AuthenticatorImp extends Authenticator {
    private final String username;
    private final String password;

    public AuthenticatorImp(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
