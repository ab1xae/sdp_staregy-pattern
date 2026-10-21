package com.university.registration.pricing;

import com.university.registration.domain.Enrollment;
import com.university.registration.rates.ExchangeRateSource;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TuitionCalculator {

    static final BigDecimal KZT_PER_CREDIT = new BigDecimal("60000");
    static final BigDecimal FLAT_SEMESTER_FEE = new BigDecimal("650000");
    static final BigDecimal USD_PER_CREDIT = new BigDecimal("150");

    private PricingMode mode;
    private final ExchangeRateSource rates;

    public TuitionCalculator(PricingMode mode, ExchangeRateSource rates) {
        this.mode = mode;
        this.rates = rates;
    }

    public static TuitionCalculator of(PricingMode mode, ExchangeRateSource rates) {
        return new TuitionCalculator(mode, rates);
    }

    public void setMode(PricingMode mode) {
        this.mode = mode;
    }

    public BigDecimal quote(Enrollment enrollment) {
        switch (mode) {
            case PER_CREDIT:
                return KZT_PER_CREDIT.multiply(BigDecimal.valueOf(enrollment.credits()));
            case FLAT_SEMESTER:
                return FLAT_SEMESTER_FEE;
            case INTERNATIONAL:
                BigDecimal usd = USD_PER_CREDIT.multiply(BigDecimal.valueOf(enrollment.credits()));
                return usd.multiply(rates.kztPerUsd()).setScale(0, RoundingMode.HALF_UP);
            default:
                throw new IllegalStateException("Unknown pricing mode: " + mode);
        }
    }
}
