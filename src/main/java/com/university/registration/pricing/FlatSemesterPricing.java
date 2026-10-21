package com.university.registration.pricing;

import com.university.registration.domain.Enrollment;
import java.math.BigDecimal;

public final class FlatSemesterPricing implements TuitionPricing {

    static final BigDecimal FLAT_SEMESTER_FEE = new BigDecimal("650000");

    @Override
    public BigDecimal tuitionFor(Enrollment enrollment) {
        return FLAT_SEMESTER_FEE;
    }

    @Override
    public String name() {
        return "Flat semester fee";
    }
}
