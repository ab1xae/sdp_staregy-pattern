package com.university.registration.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public record WaitlistEntry(Student student, LocalDateTime joinedAt) {

    public WaitlistEntry {
        Objects.requireNonNull(student, "student");
        Objects.requireNonNull(joinedAt, "joinedAt");
    }
}
