package com.university.registration.waitlist;

import static com.university.registration.waitlist.Waitlists.SAMPLE;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WaitlistPoliciesContractTest {

    static Stream<Arguments> allPolicies() {
        return Stream.<WaitlistPolicy>of(new FirstComeFirstServed(), new SeniorityFirst(), new LotteryPolicy(new Random(3)))
                .map(p -> Arguments.of(Named.of(p.name(), p)));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allPolicies")
    void picksSomeoneFromTheWaitlist(WaitlistPolicy policy) {
        for (int i = 0; i < 50; i++) {
            assertTrue(SAMPLE.contains(policy.pickNext(SAMPLE).orElseThrow()));
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allPolicies")
    void picksNobodyFromEmptyWaitlist(WaitlistPolicy policy) {
        assertTrue(policy.pickNext(List.of()).isEmpty());
    }
}
