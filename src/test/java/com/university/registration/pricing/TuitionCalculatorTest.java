package com.university.registration.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.university.registration.domain.Enrollment;
import com.university.registration.domain.Student;
import com.university.registration.rates.FixedExchangeRate;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TuitionCalculatorTest {

    private static final Student LOCAL = new Student("S1", "Aruzhan", false, 30);
    private static final Student FOREIGN = new Student("S2", "Mehmet", true, 60);

    private final FixedExchangeRate rate = new FixedExchangeRate("500.00");

    @Test
    void perCreditMultipliesCreditsByRate() {
        BigDecimal tuition = TuitionCalculator.of(PricingMode.PER_CREDIT, rate).quote(new Enrollment(LOCAL, 10));
        assertEquals(new BigDecimal("600000"), tuition);
    }

    @Test
    void flatSemesterIgnoresCreditCount() {
        TuitionCalculator calculator = TuitionCalculator.of(PricingMode.FLAT_SEMESTER, rate);
        assertEquals(new BigDecimal("650000"), calculator.quote(new Enrollment(LOCAL, 12)));
        assertEquals(new BigDecimal("650000"), calculator.quote(new Enrollment(LOCAL, 21)));
    }

    @Test
    void internationalConvertsUsdPriceAtCurrentRate() {
        BigDecimal tuition = TuitionCalculator.of(PricingMode.INTERNATIONAL, rate).quote(new Enrollment(FOREIGN, 10));
        assertEquals(new BigDecimal("750000"), tuition);
    }

    @Test
    void internationalRoundsToWholeTenge() {
        TuitionCalculator calculator = TuitionCalculator.of(PricingMode.INTERNATIONAL, new FixedExchangeRate("503.337"));
        assertEquals(new BigDecimal("75501"), calculator.quote(new Enrollment(FOREIGN, 1)));
    }
}
