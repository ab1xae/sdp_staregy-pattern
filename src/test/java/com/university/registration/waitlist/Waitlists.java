package com.university.registration.waitlist;

import com.university.registration.domain.Student;
import com.university.registration.domain.WaitlistEntry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.random.RandomGenerator;

final class Waitlists {

    static final WaitlistEntry ANA = entry("S1", "Ana", 20, 9, 0);
    static final WaitlistEntry BEK = entry("S2", "Bek", 90, 9, 10);
    static final WaitlistEntry CHOLPON = entry("S3", "Cholpon", 90, 9, 20);
    static final WaitlistEntry DIAS = entry("S4", "Dias", 45, 8, 50);

    static final List<WaitlistEntry> SAMPLE = List.of(ANA, BEK, CHOLPON, DIAS);

    private Waitlists() {
    }

    private static WaitlistEntry entry(String id, String name, int credits, int hour, int minute) {
        return new WaitlistEntry(new Student(id, name, false, credits), LocalDateTime.of(2026, 8, 20, hour, minute));
    }

    static RandomGenerator alwaysIndex(int index) {
        return new RandomGenerator() {
            @Override
            public int nextInt(int bound) {
                return index;
            }

            @Override
            public long nextLong() {
                return index;
            }
        };
    }
}
