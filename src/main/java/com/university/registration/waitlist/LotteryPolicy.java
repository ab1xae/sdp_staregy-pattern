package com.university.registration.waitlist;

import com.university.registration.domain.WaitlistEntry;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;

public final class LotteryPolicy implements WaitlistPolicy {

    private final RandomGenerator random;

    public LotteryPolicy(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public Optional<WaitlistEntry> pickNext(List<WaitlistEntry> waitlist) {
        if (waitlist.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(waitlist.get(random.nextInt(waitlist.size())));
    }

    @Override
    public String name() {
        return "Lottery";
    }
}
