package com.mruruc.mail.sender.template;

public enum Importance {
    HIGH("High"), NORMAL("Normal"), LOW("Low");

    private final String value;

    Importance(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
