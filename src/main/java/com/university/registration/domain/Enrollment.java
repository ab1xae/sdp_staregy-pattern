package com.university.registration.domain;

import java.util.Objects;

public record Enrollment(Student student, int credits) {

    public Enrollment {
        Objects.requireNonNull(student, "student");
        if (credits <= 0) {
            throw new IllegalArgumentException("Credits must be positive: " + credits);
        }
    }
}
