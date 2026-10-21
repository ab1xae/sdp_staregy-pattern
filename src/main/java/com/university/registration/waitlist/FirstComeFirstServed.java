package com.university.registration.waitlist;

import com.university.registration.domain.WaitlistEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class FirstComeFirstServed implements WaitlistPolicy {

    @Override
    public Optional<WaitlistEntry> pickNext(List<WaitlistEntry> waitlist) {
        return waitlist.stream().min(Comparator.comparing(WaitlistEntry::joinedAt));
    }

    @Override
    public String name() {
        return "First come, first served";
    }
}
