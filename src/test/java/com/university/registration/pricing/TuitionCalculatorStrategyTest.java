package com.university.registration.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.university.registration.domain.Enrollment;
import com.university.registration.domain.Student;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TuitionCalculatorStrategyTest {

    private final Enrollment enrollment = new Enrollment(new Student("S1", "Aruzhan", false, 30), 15);

    @Test
    void switchesStrategyAtRuntime() {
        TuitionCalculator calculator = new TuitionCalculator(new PerCreditPricing());
        assertEquals(new BigDecimal("900000"), calculator.quote(enrollment));

        FlatSemesterPricing flat = new FlatSemesterPricing();
        calculator.setStrategy(flat);

        assertSame(flat, calculator.strategy());
        assertEquals(new BigDecimal("650000"), calculator.quote(enrollment));
    }

    @Test
    void rejectsMissingStrategy() {
        assertThrows(NullPointerException.class, () -> new TuitionCalculator(null));
    }
}
