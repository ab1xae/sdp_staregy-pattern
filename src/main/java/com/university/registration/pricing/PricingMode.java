package com.university.registration.pricing;

import com.university.registration.rates.ExchangeRateSource;
import java.util.function.Function;

public enum PricingMode {
    PER_CREDIT(rates -> new PerCreditPricing()),
    FLAT_SEMESTER(rates -> new FlatSemesterPricing()),
    INTERNATIONAL(InternationalPricing::new);

    private final Function<ExchangeRateSource, TuitionPricing> factory;

    PricingMode(Function<ExchangeRateSource, TuitionPricing> factory) {
        this.factory = factory;
    }

    public TuitionPricing create(ExchangeRateSource rates) {
        return factory.apply(rates);
    }
}
