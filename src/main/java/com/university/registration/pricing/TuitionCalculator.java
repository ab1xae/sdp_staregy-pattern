package com.university.registration.pricing;

import com.university.registration.domain.Enrollment;
import com.university.registration.rates.ExchangeRateSource;
import java.math.BigDecimal;
import java.util.Objects;

public class TuitionCalculator {

    private TuitionPricing strategy;

    public TuitionCalculator(TuitionPricing strategy) {
        setStrategy(strategy);
    }

    public static TuitionCalculator of(PricingMode mode, ExchangeRateSource rates) {
        return new TuitionCalculator(mode.create(rates));
    }

    public void setStrategy(TuitionPricing strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public TuitionPricing strategy() {
        return strategy;
    }

    public BigDecimal quote(Enrollment enrollment) {
        return strategy.tuitionFor(enrollment);
    }
}
