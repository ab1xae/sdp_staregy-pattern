package com.university.registration.waitlist;

import com.university.registration.domain.WaitlistEntry;
import java.util.List;
import java.util.Optional;

public interface WaitlistPolicy {

    Optional<WaitlistEntry> pickNext(List<WaitlistEntry> waitlist);

    String name();
}
