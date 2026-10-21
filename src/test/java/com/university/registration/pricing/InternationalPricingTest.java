package com.university.registration.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.university.registration.domain.Enrollment;
import com.university.registration.domain.Student;
import com.university.registration.rates.FixedExchangeRate;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class InternationalPricingTest {

    private final Enrollment enrollment = new Enrollment(new Student("S2", "Mehmet", true, 60), 10);

    @Test
    void convertsUsdPriceWithRateFromSource() {
        InternationalPricing pricing = new InternationalPricing(new FixedExchangeRate("500.00"));
        assertEquals(new BigDecimal("750000"), pricing.tuitionFor(enrollment));
    }

    @Test
    void followsRateChanges() {
        InternationalPricing pricing = new InternationalPricing(new FixedExchangeRate("520.00"));
        assertEquals(new BigDecimal("780000"), pricing.tuitionFor(enrollment));
    }
}
