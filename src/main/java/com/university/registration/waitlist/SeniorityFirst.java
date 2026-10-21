package com.university.registration.waitlist;

import com.university.registration.domain.WaitlistEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class SeniorityFirst implements WaitlistPolicy {

    private static final Comparator<WaitlistEntry> MOST_SENIOR =
            Comparator.comparingInt((WaitlistEntry e) -> e.student().earnedCredits()).reversed()
                    .thenComparing(WaitlistEntry::joinedAt);

    @Override
    public Optional<WaitlistEntry> pickNext(List<WaitlistEntry> waitlist) {
        return waitlist.stream().min(MOST_SENIOR);
    }

    @Override
    public String name() {
        return "Seniority first";
    }
}
