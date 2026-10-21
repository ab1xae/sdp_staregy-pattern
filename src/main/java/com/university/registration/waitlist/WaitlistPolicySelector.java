package com.university.registration.waitlist;

import com.university.registration.domain.RegistrationPhase;
import java.util.random.RandomGenerator;

public class WaitlistPolicySelector {

    static final int HIGH_DEMAND_RATIO = 3;

    private final WaitlistPolicy seniority = new SeniorityFirst();
    private final WaitlistPolicy fifo = new FirstComeFirstServed();
    private final WaitlistPolicy lottery;

    public WaitlistPolicySelector(RandomGenerator random) {
        this.lottery = new LotteryPolicy(random);
    }

    public WaitlistPolicy select(AllocationSituation situation) {
        if (situation.phase() == RegistrationPhase.PRIORITY) {
            return seniority;
        }
        if (situation.waitlistSize() >= situation.freeSeats() * HIGH_DEMAND_RATIO) {
            return lottery;
        }
        return fifo;
    }
}
