package com.university.registration.waitlist;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.university.registration.domain.RegistrationPhase;
import java.util.Random;
import org.junit.jupiter.api.Test;

class WaitlistPolicySelectorTest {

    private final WaitlistPolicySelector selector = new WaitlistPolicySelector(new Random(1));

    @Test
    void priorityPhaseFavoursSeniors() {
        assertInstanceOf(SeniorityFirst.class, selector.select(new AllocationSituation(RegistrationPhase.PRIORITY, 2, 3)));
    }

    @Test
    void priorityRuleWinsOverHighDemand() {
        assertInstanceOf(SeniorityFirst.class, selector.select(new AllocationSituation(RegistrationPhase.PRIORITY, 2, 40)));
    }

    @Test
    void highDemandUsesLottery() {
        assertInstanceOf(LotteryPolicy.class, selector.select(new AllocationSituation(RegistrationPhase.OPEN, 2, 6)));
        assertInstanceOf(LotteryPolicy.class, selector.select(new AllocationSituation(RegistrationPhase.ADD_DROP, 1, 9)));
    }

    @Test
    void normalDemandUsesFirstComeFirstServed() {
        assertInstanceOf(FirstComeFirstServed.class, selector.select(new AllocationSituation(RegistrationPhase.OPEN, 2, 5)));
        assertInstanceOf(FirstComeFirstServed.class, selector.select(new AllocationSituation(RegistrationPhase.ADD_DROP, 3, 0)));
    }

    @Test
    void rejectsSituationWithoutFreeSeats() {
        assertThrows(IllegalArgumentException.class, () -> new AllocationSituation(RegistrationPhase.OPEN, 0, 4));
    }
}
