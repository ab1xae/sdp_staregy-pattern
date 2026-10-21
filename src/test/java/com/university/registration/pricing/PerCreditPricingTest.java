package com.university.registration.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.university.registration.domain.Enrollment;
import com.university.registration.domain.Student;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PerCreditPricingTest {

    private final PerCreditPricing pricing = new PerCreditPricing();
    private final Student student = new Student("S1", "Aruzhan", false, 30);

    @Test
    void chargesRateForEveryCredit() {
        assertEquals(new BigDecimal("60000"), pricing.tuitionFor(new Enrollment(student, 1)));
        assertEquals(new BigDecimal("420000"), pricing.tuitionFor(new Enrollment(student, 7)));
    }
}
