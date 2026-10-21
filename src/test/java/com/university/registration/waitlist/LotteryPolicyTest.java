package com.university.registration.waitlist;

import static com.university.registration.waitlist.Waitlists.ANA;
import static com.university.registration.waitlist.Waitlists.CHOLPON;
import static com.university.registration.waitlist.Waitlists.SAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LotteryPolicyTest {

    @Test
    void picksEntryAtDrawnPosition() {
        assertEquals(CHOLPON, new LotteryPolicy(Waitlists.alwaysIndex(2)).pickNext(SAMPLE).orElseThrow());
        assertEquals(ANA, new LotteryPolicy(Waitlists.alwaysIndex(0)).pickNext(SAMPLE).orElseThrow());
    }
}
