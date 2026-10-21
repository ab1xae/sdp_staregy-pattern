package com.university.registration.waitlist;

import static com.university.registration.waitlist.Waitlists.ANA;
import static com.university.registration.waitlist.Waitlists.BEK;
import static com.university.registration.waitlist.Waitlists.CHOLPON;
import static com.university.registration.waitlist.Waitlists.DIAS;
import static com.university.registration.waitlist.Waitlists.SAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.university.registration.domain.WaitlistEntry;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SeatAllocatorTest {

    @Test
    void admitsInPolicyOrderUpToFreeSeats() {
        SeatAllocator allocator = new SeatAllocator(new FirstComeFirstServed());
        assertEquals(List.of(DIAS, ANA), allocator.admit(2, SAMPLE));
    }

    @Test
    void admitsEveryoneWhenSeatsExceedWaitlist() {
        SeatAllocator allocator = new SeatAllocator(new FirstComeFirstServed());
        assertEquals(List.of(DIAS, ANA, BEK, CHOLPON), allocator.admit(10, SAMPLE));
    }

    @Test
    void switchingStrategyChangesWhoGetsSeats() {
        SeatAllocator allocator = new SeatAllocator(new FirstComeFirstServed());
        allocator.setStrategy(new SeniorityFirst());
        assertEquals(List.of(BEK, CHOLPON), allocator.admit(2, SAMPLE));
    }

    @Test
    void leavesCallerWaitlistUntouched() {
        List<WaitlistEntry> waitlist = new ArrayList<>(SAMPLE);
        new SeatAllocator(new SeniorityFirst()).admit(3, waitlist);
        assertEquals(SAMPLE, waitlist);
    }

    @Test
    void rejectsMissingStrategy() {
        SeatAllocator allocator = new SeatAllocator(new FirstComeFirstServed());
        assertThrows(NullPointerException.class, () -> allocator.setStrategy(null));
    }
}
