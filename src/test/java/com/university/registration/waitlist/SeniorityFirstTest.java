package com.university.registration.waitlist;

import static com.university.registration.waitlist.Waitlists.ANA;
import static com.university.registration.waitlist.Waitlists.BEK;
import static com.university.registration.waitlist.Waitlists.CHOLPON;
import static com.university.registration.waitlist.Waitlists.DIAS;
import static com.university.registration.waitlist.Waitlists.SAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class SeniorityFirstTest {

    private final SeniorityFirst policy = new SeniorityFirst();

    @Test
    void picksStudentWithMostEarnedCredits() {
        assertEquals(DIAS, policy.pickNext(List.of(ANA, DIAS)).orElseThrow());
    }

    @Test
    void breaksTieByEarlierJoin() {
        assertEquals(BEK, policy.pickNext(List.of(CHOLPON, BEK)).orElseThrow());
        assertEquals(BEK, policy.pickNext(SAMPLE).orElseThrow());
    }
}
