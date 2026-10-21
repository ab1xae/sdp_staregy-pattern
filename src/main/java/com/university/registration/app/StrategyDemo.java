package com.university.registration.app;

import com.university.registration.domain.Enrollment;
import com.university.registration.domain.RegistrationPhase;
import com.university.registration.domain.Student;
import com.university.registration.domain.WaitlistEntry;
import com.university.registration.pricing.FlatSemesterPricing;
import com.university.registration.pricing.InternationalPricing;
import com.university.registration.pricing.PerCreditPricing;
import com.university.registration.pricing.PricingSelector;
import com.university.registration.pricing.TuitionCalculator;
import com.university.registration.pricing.TuitionPricing;
import com.university.registration.rates.ExchangeRateSource;
import com.university.registration.rates.SimulatedExchangeRateSource;
import com.university.registration.waitlist.AllocationSituation;
import com.university.registration.waitlist.FirstComeFirstServed;
import com.university.registration.waitlist.LotteryPolicy;
import com.university.registration.waitlist.SeatAllocator;
import com.university.registration.waitlist.SeniorityFirst;
import com.university.registration.waitlist.WaitlistPolicy;
import com.university.registration.waitlist.WaitlistPolicySelector;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class StrategyDemo {

    static Student aruzhan = new Student("S1", "Aruzhan", false, 30);
    static Student dias = new Student("S2", "Dias", false, 95);
    static Student mehmet = new Student("S3", "Mehmet", true, 60);
    static Student sofia = new Student("S4", "Sofia", false, 12);
    static Student timur = new Student("S5", "Timur", false, 110);

    static List<WaitlistEntry> waitlist = List.of(
            entry(sofia, 8, 5),
            entry(dias, 9, 40),
            entry(aruzhan, 8, 30),
            entry(timur, 10, 15),
            entry(mehmet, 9, 0));

    static Random random = new Random();
    static ExchangeRateSource rates = new SimulatedExchangeRateSource();
    static List<TuitionPricing> pricings = List.of(
            new PerCreditPricing(), new FlatSemesterPricing(), new InternationalPricing(rates));
    static List<WaitlistPolicy> policies = List.of(
            new FirstComeFirstServed(), new SeniorityFirst(), new LotteryPolicy(random));

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("1 - All strategies");
            System.out.println("2 - Manual vs selector");
            System.out.println("3 - Change strategy");
            System.out.println("0 - Exit");
            System.out.print("Choose: ");
            if (!in.hasNextLine()) {
                return;
            }
            String choice = in.nextLine().trim();
            if (choice.equals("1")) {
                allStrategies();
            } else if (choice.equals("2")) {
                manualVsSelector();
            } else if (choice.equals("3")) {
                changeStrategy();
            } else if (choice.equals("0")) {
                return;
            } else {
                System.out.println("Wrong choice");
            }
        }
    }

    static void allStrategies() {
        Enrollment enrollment = new Enrollment(aruzhan, 15);
        System.out.println("Aruzhan, 15 credits:");
        for (TuitionPricing pricing : pricings) {
            System.out.println("  " + pricing.name() + ": " + pricing.tuitionFor(enrollment) + " KZT");
        }

        System.out.println("Waitlist " + names(waitlist) + ", 2 free seats:");
        for (WaitlistPolicy policy : policies) {
            System.out.println("  " + policy.name() + ": " + names(new SeatAllocator(policy).admit(2, waitlist)));
        }
    }

    static void manualVsSelector() {
        TuitionCalculator manual = new TuitionCalculator(new PerCreditPricing());
        PricingSelector selector = new PricingSelector(rates);
        List<Enrollment> enrollments = List.of(
                new Enrollment(aruzhan, 9), new Enrollment(dias, 18), new Enrollment(mehmet, 15));
        for (Enrollment e : enrollments) {
            TuitionPricing chosen = selector.select(e);
            System.out.println(e.student().name() + ", " + e.credits() + " credits:");
            System.out.println("  manual (Per credit): " + manual.quote(e) + " KZT");
            System.out.println("  selector (" + chosen.name() + "): " + chosen.tuitionFor(e) + " KZT");
        }

        SeatAllocator manualSeats = new SeatAllocator(new FirstComeFirstServed());
        WaitlistPolicySelector policySelector = new WaitlistPolicySelector(random);
        List<AllocationSituation> situations = List.of(
                new AllocationSituation(RegistrationPhase.PRIORITY, 2, waitlist.size()),
                new AllocationSituation(RegistrationPhase.OPEN, 1, waitlist.size()),
                new AllocationSituation(RegistrationPhase.ADD_DROP, 2, waitlist.size()));
        for (AllocationSituation s : situations) {
            WaitlistPolicy chosen = policySelector.select(s);
            System.out.println(s.phase() + ", " + s.freeSeats() + " free seats:");
            System.out.println("  manual (First come, first served): " + names(manualSeats.admit(s.freeSeats(), waitlist)));
            System.out.println("  selector (" + chosen.name() + "): " + names(new SeatAllocator(chosen).admit(s.freeSeats(), waitlist)));
        }
    }

    static void changeStrategy() {
        Enrollment enrollment = new Enrollment(mehmet, 12);
        TuitionCalculator calculator = new TuitionCalculator(pricings.get(0));
        System.out.println("Mehmet, 12 credits:");
        for (TuitionPricing pricing : pricings) {
            calculator.setStrategy(pricing);
            System.out.println("  " + pricing.name() + ": " + calculator.quote(enrollment) + " KZT");
        }

        SeatAllocator allocator = new SeatAllocator(policies.get(0));
        System.out.println("3 free seats:");
        for (WaitlistPolicy policy : policies) {
            allocator.setStrategy(policy);
            System.out.println("  " + policy.name() + ": " + names(allocator.admit(3, waitlist)));
        }
    }

    static WaitlistEntry entry(Student student, int hour, int minute) {
        return new WaitlistEntry(student, LocalDateTime.of(2026, 8, 20, hour, minute));
    }

    static List<String> names(List<WaitlistEntry> entries) {
        List<String> result = new ArrayList<>();
        for (WaitlistEntry e : entries) {
            result.add(e.student().name());
        }
        return result;
    }
}
