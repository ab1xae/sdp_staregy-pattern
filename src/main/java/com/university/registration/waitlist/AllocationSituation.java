package com.university.registration.waitlist;

import com.university.registration.domain.RegistrationPhase;
import java.util.Objects;

public record AllocationSituation(RegistrationPhase phase, int freeSeats, int waitlistSize) {

    public AllocationSituation {
        Objects.requireNonNull(phase, "phase");
        if (freeSeats <= 0 || waitlistSize < 0) {
            throw new IllegalArgumentException("Invalid seats/waitlist: " + freeSeats + "/" + waitlistSize);
        }
    }
}
