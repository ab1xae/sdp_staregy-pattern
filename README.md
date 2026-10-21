# Assignment 5 — Strategy Pattern

Domain: university course registration.

The project shows two tasks that can be done in different ways:

1. **Tuition pricing.** How much a student pays for a semester.
2. **Waitlist.** Which student gets a free seat in a full course.

Each way is a separate class (a *strategy*). The program can switch between them while it runs.

## How to run

You need JDK 17 or newer. Maven is included (`mvnw`).

**IntelliJ IDEA**
- Open the project folder. IntelliJ detects `pom.xml` by itself.
- Demo: open `src/main/java/.../app/StrategyDemo.java`, click the green ▶ next to `main`.
- Tests: right-click `src/test/java` → *Run 'All Tests'*.

**Command line** (on Windows use `mvnw.cmd` instead of `./mvnw`)
```bash
./mvnw test
./mvnw -q compile exec:java
./mvnw -q compile exec:java -Dexec.args="--all"
```
1st line runs the 44 tests. 2nd opens the menu. 3rd prints every demo section without the menu.

## Project structure

```
src/main/java/com/university/registration/
├── domain/      data: Student, Enrollment, WaitlistEntry, RegistrationPhase
├── rates/       exchange rate behind an interface (external data)
├── pricing/     family 1: tuition pricing strategies + context + selector
├── waitlist/    family 2: waitlist strategies + context + selector
└── app/         StrategyDemo, the console menu

src/test/java/com/university/registration/   tests for each package (same layout)
```

How one family works:

```
Selector ──chooses──▶ Strategy (interface) ◀── Context
                         ▲                      setStrategy()
           ┌─────────────┼─────────────┐        calls the strategy
     Strategy A     Strategy B     Strategy C
```

| | Pricing | Waitlist |
|---|---|---|
| Interface | `TuitionPricing` | `WaitlistPolicy` |
| Strategies | `PerCreditPricing`, `FlatSemesterPricing`, `InternationalPricing` | `FirstComeFirstServed`, `SeniorityFirst`, `LotteryPolicy` |
| Context | `TuitionCalculator` | `SeatAllocator` |
| Selector | `PricingSelector` | `WaitlistPolicySelector` |

Selector rules:
- **Pricing:** international student → `InternationalPricing`; 12+ credits → `FlatSemesterPricing`; otherwise → `PerCreditPricing`.
- **Waitlist:** priority week → `SeniorityFirst`; waitlist at least 3× the free seats → `LotteryPolicy`; otherwise → `FirstComeFirstServed`.

`if` is used only in the selectors. The contexts never check which strategy they hold.

## Classes and responsibilities

| Class | Responsibility |
|---|---|
| `Student` | Holds identity, residency status plus earned credits of one student. |
| `Enrollment` | Pairs a student with the credit load requested for one semester. |
| `WaitlistEntry` | Records which student joined a course waitlist at what moment. |
| `RegistrationPhase` | Names the current stage of the registration calendar. |
| `ExchangeRateSource` | Hides where the current KZT-per-USD rate comes from. |
| `SimulatedExchangeRateSource` | Produces a randomly drifting KZT-per-USD rate near 500. |
| `TuitionPricing` | Declares how semester tuition gets computed for one enrollment. |
| `PerCreditPricing` | Charges 60,000 KZT for every enrolled credit. |
| `FlatSemesterPricing` | Charges one fixed 650,000 KZT semester fee whatever the credit load. |
| `InternationalPricing` | Converts 150 USD per credit into tenge using the current exchange rate. |
| `PricingMode` | Keeps the old pre-refactoring entry point working by mapping names to strategies. |
| `TuitionCalculator` | Quotes tuition by delegating to the current pricing strategy. |
| `PricingSelector` | Picks the pricing strategy matching student residency plus credit load. |
| `AllocationSituation` | Describes the registration phase, free seats, waitlist length at one moment. |
| `WaitlistPolicy` | Declares how the next student gets picked from a waitlist. |
| `FirstComeFirstServed` | Picks the student who joined the waitlist earliest. |
| `SeniorityFirst` | Picks the student with the most earned credits; earlier joining breaks ties. |
| `LotteryPolicy` | Picks a random student from the waitlist. |
| `SeatAllocator` | Fills free seats one by one, asking the current policy who goes next. |
| `WaitlistPolicySelector` | Picks the waitlist policy matching the registration phase plus current demand. |
| `StrategyDemo` | Shows every strategy on shared input, manual versus automatic choice, plus runtime switching. |
| `FixedExchangeRate` (test) | Returns one constant rate, keeping tests predictable. |

## External data

`InternationalPricing` needs the USD → KZT exchange rate. It does not know where the rate comes from: it only uses the `ExchangeRateSource` interface.
- In the demo: `SimulatedExchangeRateSource` (random rate around 500).
- In tests: `FixedExchangeRate` (always the same rate).

## Why Strategy, not State or Template Method

**Not State.** In State, an object changes its own behaviour when its state changes, and states switch to each other. Here the calculator never changes its algorithm by itself. The user or the selector picks the algorithm from outside, and the strategies know nothing about each other.

**Not Template Method.** Template Method keeps one algorithm skeleton in a base class, and subclasses replace some steps. It is fixed at compile time through inheritance. Our strategies have no common skeleton (multiply by credits, a fixed fee, a currency conversion; earliest, most senior, random). They are separate objects behind one interface, and they can be replaced while the program runs with `setStrategy()`.

**Why Strategy.** One task, several interchangeable algorithms, chosen at runtime. A new algorithm is a new class; the context and the other strategies do not change (Open/Closed Principle).

## Refactoring commits (pricing family)

- **Before:** [`dc9541d`](https://github.com/ab1xae/sdp_staregy-pattern/commit/dc9541d8ff2cde4b575601358577f2932be00b03): all three prices in one class with `switch`.
- **After:** [`08e2abc`](https://github.com/ab1xae/sdp_staregy-pattern/commit/08e2abc6897eabf7cd882cbb4c96e4a79f3d46c3): one interface plus three strategy classes.

`TuitionCalculatorTest` is the same file in both commits, and it passes on both.

## Tests

- One test class for every strategy.
- Every selector rule (`PricingSelectorTest`, `WaitlistPolicySelectorTest`).
- `@ParameterizedTest` over all strategies of a family: price is never negative; the chosen student is always from the waitlist.
- Contexts, `setStrategy()`, the exchange rate source, the domain checks.
