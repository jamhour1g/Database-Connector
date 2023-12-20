package com.jamhour.data;

import lombok.Getter;

@Getter
public enum EnrollmentStatus {

    ACTIVE("active"),
    COMPLETED("completed"),
    DROPPED("dropped"),
    WITHDRAWN("withdrawn");

    private final String status;

    EnrollmentStatus(String status) {
        this.status = status;
    }
}
