package com.university.registration.pricing;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.university.registration.domain.Enrollment;
import com.university.registration.domain.Student;
import com.university.registration.rates.FixedExchangeRate;
import org.junit.jupiter.api.Test;

class PricingSelectorTest {

    private final PricingSelector selector = new PricingSelector(new FixedExchangeRate("500.00"));
    private final Student local = new Student("S1", "Aruzhan", false, 30);
    private final Student foreign = new Student("S2", "Mehmet", true, 60);

    @Test
    void internationalStudentGetsInternationalPricing() {
        assertInstanceOf(InternationalPricing.class, selector.select(new Enrollment(foreign, 6)));
    }

    @Test
    void internationalRuleWinsOverFullTimeLoad() {
        assertInstanceOf(InternationalPricing.class, selector.select(new Enrollment(foreign, 18)));
    }

    @Test
    void fullTimeLocalStudentGetsFlatFee() {
        assertInstanceOf(FlatSemesterPricing.class, selector.select(new Enrollment(local, 12)));
    }

    @Test
    void partTimeLocalStudentPaysPerCredit() {
        assertInstanceOf(PerCreditPricing.class, selector.select(new Enrollment(local, 11)));
    }
}
