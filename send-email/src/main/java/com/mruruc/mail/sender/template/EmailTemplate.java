package com.mruruc.mail.sender.template;

public enum EmailTemplate {
    ACCOUNT_ACTIVATION("account-activation"),
    WELCOME_MESSAGE("welcome");

    private final String name;

    EmailTemplate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
