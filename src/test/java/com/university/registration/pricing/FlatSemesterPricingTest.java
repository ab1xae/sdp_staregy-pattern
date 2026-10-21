package com.university.registration.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.university.registration.domain.Enrollment;
import com.university.registration.domain.Student;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FlatSemesterPricingTest {

    private final FlatSemesterPricing pricing = new FlatSemesterPricing();
    private final Student student = new Student("S1", "Aruzhan", false, 30);

    @Test
    void chargesSameFeeWhateverTheLoad() {
        assertEquals(new BigDecimal("650000"), pricing.tuitionFor(new Enrollment(student, 12)));
        assertEquals(new BigDecimal("650000"), pricing.tuitionFor(new Enrollment(student, 24)));
    }
}
