package com.university.registration.pricing;

import com.university.registration.domain.Enrollment;
import java.math.BigDecimal;

public interface TuitionPricing {

    BigDecimal tuitionFor(Enrollment enrollment);

    String name();
}
