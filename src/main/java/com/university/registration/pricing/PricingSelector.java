package com.university.registration.pricing;

import com.university.registration.domain.Enrollment;
import com.university.registration.rates.ExchangeRateSource;

public class PricingSelector {

    static final int FULL_TIME_CREDITS = 12;

    private final TuitionPricing perCredit = new PerCreditPricing();
    private final TuitionPricing flatSemester = new FlatSemesterPricing();
    private final TuitionPricing international;

    public PricingSelector(ExchangeRateSource rates) {
        this.international = new InternationalPricing(rates);
    }

    public TuitionPricing select(Enrollment enrollment) {
        if (enrollment.student().international()) {
            return international;
        }
        if (enrollment.credits() >= FULL_TIME_CREDITS) {
            return flatSemester;
        }
        return perCredit;
    }
}
