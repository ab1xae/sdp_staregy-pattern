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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class StrategyDemo {

    private static final Student ARUZHAN = new Student("S1", "Aruzhan", false, 30);
    private static final Student DIAS = new Student("S2", "Dias", false, 95);
    private static final Student MEHMET = new Student("S3", "Mehmet", true, 60);
    private static final Student SOFIA = new Student("S4", "Sofia", false, 12);
    private static final Student TIMUR = new Student("S5", "Timur", false, 110);

    private static final List<WaitlistEntry> WAITLIST = List.of(
            entry(SOFIA, 8, 5),
            entry(DIAS, 9, 40),
            entry(ARUZHAN, 8, 30),
            entry(TIMUR, 10, 15),
            entry(MEHMET, 9, 0));

    private final ExchangeRateSource rates;
    private final Random random;
    private final List<TuitionPricing> pricings;
    private final List<WaitlistPolicy> policies;

    StrategyDemo(ExchangeRateSource rates, Random random) {
        this.rates = rates;
        this.random = random;
        this.pricings = List.of(new PerCreditPricing(), new FlatSemesterPricing(), new InternationalPricing(rates));
        this.policies = List.of(new FirstComeFirstServed(), new SeniorityFirst(), new LotteryPolicy(random));
    }

    public static void main(String[] args) {
        StrategyDemo demo = new StrategyDemo(new SimulatedExchangeRateSource(), new Random());
        if (args.length > 0 && args[0].equals("--all")) {
            demo.runAll();
            return;
        }
        demo.menu(new Scanner(System.in));
    }

    private void menu(Scanner in) {
        while (true) {
            System.out.println();
            System.out.println("=== University Registration: Strategy demo ===");
            System.out.println("1) Same input through every strategy");
            System.out.println("2) Manual choice vs Selector");
            System.out.println("3) Switch strategy at runtime");
            System.out.println("4) Run all");
            System.out.println("0) Exit");
            System.out.print("> ");
            if (!in.hasNextLine()) {
                return;
            }
            switch (in.nextLine().trim()) {
                case "1" -> sameInputThroughEveryStrategy();
                case "2" -> manualVersusSelector();
                case "3" -> switchAtRuntime();
                case "4" -> runAll();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Unknown option");
            }
        }
    }

    void runAll() {
        sameInputThroughEveryStrategy();
        manualVersusSelector();
        switchAtRuntime();
    }

    void sameInputThroughEveryStrategy() {
        header("1. Same input through every strategy");
        Enrollment enrollment = new Enrollment(ARUZHAN, 15);
        System.out.println("Tuition for " + describe(enrollment) + ":");
        for (TuitionPricing pricing : pricings) {
            System.out.printf("  %-22s %s%n", pricing.name(), money(pricing.tuitionFor(enrollment)));
        }

        System.out.println();
        System.out.println("2 free seats, waitlist: " + names(WAITLIST));
        SeatAllocator allocator = new SeatAllocator(policies.get(0));
        for (WaitlistPolicy policy : policies) {
            allocator.setStrategy(policy);
            System.out.printf("  %-26s admits %s%n", policy.name(), names(allocator.admit(2, WAITLIST)));
        }
    }

    void manualVersusSelector() {
        header("2. Manual choice vs Selector");
        TuitionCalculator manual = new TuitionCalculator(new PerCreditPricing());
        PricingSelector selector = new PricingSelector(rates);
        System.out.printf("  %-28s %-24s %-22s %s%n", "Enrollment", "Manual: Per credit", "Selector picks", "Auto price");
        for (Enrollment enrollment : List.of(
                new Enrollment(ARUZHAN, 9),
                new Enrollment(DIAS, 18),
                new Enrollment(MEHMET, 15))) {
            TuitionCalculator auto = new TuitionCalculator(selector.select(enrollment));
            System.out.printf("  %-28s %-24s %-22s %s%n", describe(enrollment), money(manual.quote(enrollment)),
                    auto.strategy().name(), money(auto.quote(enrollment)));
        }

        System.out.println();
        SeatAllocator manualSeats = new SeatAllocator(new FirstComeFirstServed());
        WaitlistPolicySelector policySelector = new WaitlistPolicySelector(random);
        for (AllocationSituation situation : List.of(
                new AllocationSituation(RegistrationPhase.PRIORITY, 2, WAITLIST.size()),
                new AllocationSituation(RegistrationPhase.OPEN, 1, WAITLIST.size()),
                new AllocationSituation(RegistrationPhase.ADD_DROP, 2, WAITLIST.size()))) {
            SeatAllocator auto = new SeatAllocator(policySelector.select(situation));
            System.out.printf("  %-9s %d seat(s) | manual FCFS -> %-18s | selector: %-24s -> %s%n",
                    situation.phase(), situation.freeSeats(),
                    names(manualSeats.admit(situation.freeSeats(), WAITLIST)),
                    auto.strategy().name(), names(auto.admit(situation.freeSeats(), WAITLIST)));
        }
    }

    void switchAtRuntime() {
        header("3. Switch strategy at runtime");
        Enrollment enrollment = new Enrollment(MEHMET, 12);
        TuitionCalculator calculator = new TuitionCalculator(pricings.get(0));
        System.out.println("One TuitionCalculator, " + describe(enrollment) + ":");
        for (TuitionPricing pricing : pricings) {
            calculator.setStrategy(pricing);
            System.out.printf("  setStrategy(%s) -> %s%n", pricing.name(), money(calculator.quote(enrollment)));
        }

        System.out.println("One SeatAllocator, 3 free seats:");
        SeatAllocator allocator = new SeatAllocator(policies.get(0));
        for (WaitlistPolicy policy : policies) {
            allocator.setStrategy(policy);
            System.out.printf("  setStrategy(%s) -> %s%n", policy.name(), names(allocator.admit(3, WAITLIST)));
        }
    }

    private static WaitlistEntry entry(Student student, int hour, int minute) {
        return new WaitlistEntry(student, LocalDateTime.of(2026, 8, 20, hour, minute));
    }

    private static void header(String title) {
        System.out.println();
        System.out.println("--- " + title + " ---");
    }

    private static String describe(Enrollment enrollment) {
        Student s = enrollment.student();
        return s.name() + (s.international() ? " (intl)" : "") + ", " + enrollment.credits() + " cr";
    }

    private static String money(BigDecimal amount) {
        return String.format(Locale.ROOT, "%,d KZT", amount.longValueExact());
    }

    private static String names(List<WaitlistEntry> entries) {
        return entries.stream().map(e -> e.student().name()).collect(Collectors.joining(", ", "[", "]"));
    }
}
