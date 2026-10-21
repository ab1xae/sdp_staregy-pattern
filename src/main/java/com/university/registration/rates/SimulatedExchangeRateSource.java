package com.university.registration.rates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.random.RandomGenerator;

public final class SimulatedExchangeRateSource implements ExchangeRateSource {

    static final BigDecimal BASE_RATE = new BigDecimal("500.00");
    static final double MAX_SWING = 0.03;

    private final RandomGenerator random;

    public SimulatedExchangeRateSource() {
        this(new Random());
    }

    public SimulatedExchangeRateSource(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public BigDecimal kztPerUsd() {
        double swing = (random.nextDouble() * 2 - 1) * MAX_SWING;
        return BASE_RATE.multiply(BigDecimal.valueOf(1 + swing)).setScale(2, RoundingMode.HALF_UP);
    }
}
