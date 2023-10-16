package com.example.SangueAmigo.model.email;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmailStatus {

    SENT("sent"),
    ERROR("error");

    private final String value;

    EmailStatus(String value){
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static EmailStatus fromEmailStatus(String status) {
        for (EmailStatus s : values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid EmailStatus: " + status);
    }
}