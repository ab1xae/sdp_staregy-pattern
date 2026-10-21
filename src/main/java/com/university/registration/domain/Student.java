package com.university.registration.domain;

import java.util.Objects;

public record Student(String id, String name, boolean international, int earnedCredits) {

    public Student {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        if (earnedCredits < 0) {
            throw new IllegalArgumentException("Earned credits cannot be negative: " + earnedCredits);
        }
    }
}
