package com.university.registration.waitlist;

import com.university.registration.domain.WaitlistEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeatAllocator {

    private WaitlistPolicy strategy;

    public SeatAllocator(WaitlistPolicy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(WaitlistPolicy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public WaitlistPolicy strategy() {
        return strategy;
    }

    public List<WaitlistEntry> admit(int freeSeats, List<WaitlistEntry> waitlist) {
        List<WaitlistEntry> remaining = new ArrayList<>(waitlist);
        List<WaitlistEntry> admitted = new ArrayList<>();
        while (admitted.size() < freeSeats && !remaining.isEmpty()) {
            WaitlistEntry next = strategy.pickNext(remaining).orElseThrow();
            remaining.remove(next);
            admitted.add(next);
        }
        return admitted;
    }
}
