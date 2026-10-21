package com.university.registration.waitlist;

import static com.university.registration.waitlist.Waitlists.DIAS;
import static com.university.registration.waitlist.Waitlists.SAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FirstComeFirstServedTest {

    @Test
    void picksEarliestJoiner() {
        assertEquals(DIAS, new FirstComeFirstServed().pickNext(SAMPLE).orElseThrow());
    }
}
