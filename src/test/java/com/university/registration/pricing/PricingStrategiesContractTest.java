package com.university.registration.pricing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.university.registration.domain.Enrollment;
import com.university.registration.domain.Student;
import com.university.registration.rates.FixedExchangeRate;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PricingStrategiesContractTest {

    static Stream<Arguments> allStrategies() {
        return Stream.<TuitionPricing>of(
                        new PerCreditPricing(),
                        new FlatSemesterPricing(),
                        new InternationalPricing(new FixedExchangeRate("500.00")))
                .map(p -> Arguments.of(Named.of(p.name(), p)));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allStrategies")
    void tuitionIsNeverNegative(TuitionPricing pricing) {
        Student student = new Student("S1", "Aruzhan", true, 30);
        for (int credits = 1; credits <= 30; credits++) {
            BigDecimal tuition = pricing.tuitionFor(new Enrollment(student, credits));
            assertTrue(tuition.signum() >= 0, pricing.name() + " gave " + tuition + " for " + credits);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allStrategies")
    void hasDisplayName(TuitionPricing pricing) {
        assertFalse(pricing.name().isBlank());
    }
}
