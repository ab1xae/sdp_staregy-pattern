package com.university.registration.rates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SimulatedExchangeRateSourceTest {

    @Test
    void rateStaysWithinSwingAroundBase() {
        SimulatedExchangeRateSource source = new SimulatedExchangeRateSource(new Random(7));
        BigDecimal low = new BigDecimal("485.00");
        BigDecimal high = new BigDecimal("515.00");
        for (int i = 0; i < 1_000; i++) {
            BigDecimal rate = source.kztPerUsd();
            assertTrue(rate.compareTo(low) >= 0 && rate.compareTo(high) <= 0, "rate out of band: " + rate);
        }
    }

    @Test
    void rateHasTwoDecimals() {
        assertEquals(2, new SimulatedExchangeRateSource(new Random(1)).kztPerUsd().scale());
    }
}
