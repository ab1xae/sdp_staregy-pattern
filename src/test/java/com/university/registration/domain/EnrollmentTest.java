package com.university.registration.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EnrollmentTest {

    private final Student student = new Student("S1", "Aruzhan", false, 30);

    @Test
    void rejectsZeroCredits() {
        assertThrows(IllegalArgumentException.class, () -> new Enrollment(student, 0));
    }

    @Test
    void rejectsNegativeEarnedCredits() {
        assertThrows(IllegalArgumentException.class, () -> new Student("S2", "Dias", false, -1));
    }
}
