package com.university.registration.pricing;

import com.university.registration.domain.Enrollment;
import com.university.registration.rates.ExchangeRateSource;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class InternationalPricing implements TuitionPricing {

    static final BigDecimal USD_PER_CREDIT = new BigDecimal("150");

    private final ExchangeRateSource rates;

    public InternationalPricing(ExchangeRateSource rates) {
        this.rates = rates;
    }

    @Override
    public BigDecimal tuitionFor(Enrollment enrollment) {
        BigDecimal usd = USD_PER_CREDIT.multiply(BigDecimal.valueOf(enrollment.credits()));
        return usd.multiply(rates.kztPerUsd()).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public String name() {
        return "International (USD)";
    }
}
