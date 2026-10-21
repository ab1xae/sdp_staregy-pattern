package com.university.registration.pricing;

import com.university.registration.domain.Enrollment;
import java.math.BigDecimal;

public final class PerCreditPricing implements TuitionPricing {

    static final BigDecimal KZT_PER_CREDIT = new BigDecimal("60000");

    @Override
    public BigDecimal tuitionFor(Enrollment enrollment) {
        return KZT_PER_CREDIT.multiply(BigDecimal.valueOf(enrollment.credits()));
    }

    @Override
    public String name() {
        return "Per credit";
    }
}
